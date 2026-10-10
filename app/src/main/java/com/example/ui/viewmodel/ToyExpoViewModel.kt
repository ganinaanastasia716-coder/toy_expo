package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.ToyDatabase
import com.example.data.local.UserExhibitionEntity
import com.example.data.model.AttendanceStatus
import com.example.data.model.DropStatus
import com.example.data.model.ExclusiveDropItem
import com.example.data.model.Exhibition
import com.example.data.model.Region
import com.example.data.model.ToyCategory
import com.example.data.repository.ExhibitionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

enum class NavTab(val titleRu: String, val iconName: String) {
    EXPOS("Выставки", "calendar"),
    DROPS_RADAR("Радар дропов", "radar"),
    CITIES("Карта & Города", "map"),
    MY_TRIPS("Мои поездки", "backpack")
}

data class ToyExpoUiState(
    val exhibitions: List<Exhibition> = emptyList(),
    val allExhibitions: List<Exhibition> = emptyList(),
    val grailDrops: List<ExclusiveDropItem> = emptyList(),
    val searchQuery: String = "",
    val selectedRegion: Region = Region.ALL,
    val selectedCategory: ToyCategory = ToyCategory.ALL,
    val onlyFavorites: Boolean = false,
    val onlyMyTrips: Boolean = false,
    val selectedExhibition: Exhibition? = null,
    val showAddExhibitionDialog: Boolean = false,
    val showAddDropDialog: Boolean = false,
    val currentTab: NavTab = NavTab.EXPOS,
    val toastMessage: String? = null
)

class ToyExpoViewModel(
    application: Application,
    private val repository: ExhibitionRepository
) : AndroidViewModel(application) {

    private val _searchQuery = MutableStateFlow("")
    private val _selectedRegion = MutableStateFlow(Region.ALL)
    private val _selectedCategory = MutableStateFlow(ToyCategory.ALL)
    private val _onlyFavorites = MutableStateFlow(false)
    private val _onlyMyTrips = MutableStateFlow(false)
    private val _selectedExhibitionId = MutableStateFlow<String?>(null)
    private val _showAddExhibitionDialog = MutableStateFlow(false)
    private val _showAddDropDialog = MutableStateFlow(false)
    private val _currentTab = MutableStateFlow(NavTab.EXPOS)
    private val _toastMessage = MutableStateFlow<String?>(null)

    val uiState: StateFlow<ToyExpoUiState> = combine(
        repository.getAllExhibitions(),
        repository.getAllGrailDrops(),
        _searchQuery,
        _selectedRegion,
        _selectedCategory,
        _onlyFavorites,
        _onlyMyTrips,
        _selectedExhibitionId,
        _showAddExhibitionDialog,
        _showAddDropDialog,
        _currentTab,
        _toastMessage
    ) { args: Array<Any?> ->
        @Suppress("UNCHECKED_CAST")
        val allExpos = args[0] as List<Exhibition>
        @Suppress("UNCHECKED_CAST")
        val drops = args[1] as List<ExclusiveDropItem>
        val query = args[2] as String
        val region = args[3] as Region
        val category = args[4] as ToyCategory
        val onlyFavs = args[5] as Boolean
        val onlyTrips = args[6] as Boolean
        val selectedId = args[7] as String?
        val showAddExpo = args[8] as Boolean
        val showAddDrop = args[9] as Boolean
        val tab = args[10] as NavTab
        val toast = args[11] as String?

        val filtered = allExpos.filter { expo ->
            val matchesRegion = (region == Region.ALL || expo.region == region)
            val matchesCategory = (category == ToyCategory.ALL || expo.category == category)
            val matchesSearch = query.isEmpty() ||
                    expo.name.contains(query, ignoreCase = true) ||
                    expo.city.contains(query, ignoreCase = true) ||
                    expo.country.contains(query, ignoreCase = true) ||
                    expo.featuredArtists.any { it.contains(query, ignoreCase = true) } ||
                    expo.featuredBrands.any { it.contains(query, ignoreCase = true) }
            val matchesFav = !onlyFavs || expo.isFavorite
            val matchesTrips = !onlyTrips || (expo.attendance != AttendanceStatus.NONE)

            matchesRegion && matchesCategory && matchesSearch && matchesFav && matchesTrips
        }

        val selectedExpo = allExpos.firstOrNull { it.id == selectedId }

        ToyExpoUiState(
            exhibitions = filtered,
            allExhibitions = allExpos,
            grailDrops = drops,
            searchQuery = query,
            selectedRegion = region,
            selectedCategory = category,
            onlyFavorites = onlyFavs,
            onlyMyTrips = onlyTrips,
            selectedExhibition = selectedExpo,
            showAddExhibitionDialog = showAddExpo,
            showAddDropDialog = showAddDrop,
            currentTab = tab,
            toastMessage = toast
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ToyExpoUiState()
    )

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onRegionSelect(region: Region) {
        _selectedRegion.value = region
    }

    fun onCategorySelect(category: ToyCategory) {
        _selectedCategory.value = category
    }

    fun onToggleFavoritesOnly() {
        _onlyFavorites.value = !_onlyFavorites.value
    }

    fun onToggleMyTripsOnly() {
        _onlyMyTrips.value = !_onlyMyTrips.value
    }

    fun onSelectExhibition(exhibition: Exhibition) {
        _selectedExhibitionId.value = exhibition.id
    }

    fun onClearSelectedExhibition() {
        _selectedExhibitionId.value = null
    }

    fun onSetTab(tab: NavTab) {
        _currentTab.value = tab
    }

    fun setShowAddExhibitionDialog(show: Boolean) {
        _showAddExhibitionDialog.value = show
    }

    fun setShowAddDropDialog(show: Boolean) {
        _showAddDropDialog.value = show
    }

    fun clearToast() {
        _toastMessage.value = null
    }

    fun onToggleFavorite(exhibition: Exhibition) {
        viewModelScope.launch {
            repository.updateTracking(
                exhibitionId = exhibition.id,
                isFavorite = !exhibition.isFavorite,
                attendance = exhibition.attendance,
                userNotes = exhibition.userNotes,
                plannedBudget = exhibition.plannedBudget,
                budgetCurrency = exhibition.budgetCurrency,
                completedChecklist = exhibition.completedChecklist
            )
            _toastMessage.value = if (!exhibition.isFavorite) {
                "Добавлено в избранное ❤️"
            } else {
                "Удалено из избранного"
            }
        }
    }

    fun onUpdateAttendance(exhibition: Exhibition, status: AttendanceStatus) {
        viewModelScope.launch {
            repository.updateTracking(
                exhibitionId = exhibition.id,
                isFavorite = exhibition.isFavorite,
                attendance = status,
                userNotes = exhibition.userNotes,
                plannedBudget = exhibition.plannedBudget,
                budgetCurrency = exhibition.budgetCurrency,
                completedChecklist = exhibition.completedChecklist
            )
            _toastMessage.value = "Статус обновлен: ${status.titleRu}"
        }
    }

    fun onUpdateNotesAndBudget(
        exhibition: Exhibition,
        notes: String,
        budget: Double,
        currency: String
    ) {
        viewModelScope.launch {
            repository.updateTracking(
                exhibitionId = exhibition.id,
                isFavorite = exhibition.isFavorite,
                attendance = exhibition.attendance,
                userNotes = notes,
                plannedBudget = budget,
                budgetCurrency = currency,
                completedChecklist = exhibition.completedChecklist
            )
            _toastMessage.value = "Заметки и бюджет сохранены ✍️"
        }
    }

    fun onToggleChecklistItem(exhibition: Exhibition, itemTitle: String) {
        viewModelScope.launch {
            val currentList = exhibition.completedChecklist.toMutableList()
            if (currentList.contains(itemTitle)) {
                currentList.remove(itemTitle)
            } else {
                currentList.add(itemTitle)
            }
            repository.updateTracking(
                exhibitionId = exhibition.id,
                isFavorite = exhibition.isFavorite,
                attendance = exhibition.attendance,
                userNotes = exhibition.userNotes,
                plannedBudget = exhibition.plannedBudget,
                budgetCurrency = exhibition.budgetCurrency,
                completedChecklist = currentList
            )
        }
    }

    fun onAddCustomExhibition(
        name: String,
        edition: String,
        city: String,
        country: String,
        region: Region,
        venue: String,
        startDate: String,
        endDate: String,
        category: ToyCategory,
        description: String,
        ticketPrice: String,
        website: String,
        artists: String,
        brands: String,
        raffleTips: String
    ) {
        viewModelScope.launch {
            val entity = UserExhibitionEntity(
                id = "custom_" + UUID.randomUUID().toString().take(8),
                name = name,
                edition = edition.ifEmpty { "2026/2027" },
                city = city,
                country = country,
                regionStr = region.name,
                venue = venue,
                startDate = startDate,
                endDate = endDate.ifEmpty { startDate },
                displayDates = "$startDate – $endDate",
                categoryStr = category.name,
                description = description,
                ticketInfo = "Ориентировочная цена: $ticketPrice",
                ticketPriceEstimate = ticketPrice,
                officialWebsite = website,
                featuredArtistsStr = artists,
                featuredBrandsStr = brands,
                lotteryAndRaffleTips = raffleTips
            )
            repository.addCustomExhibition(entity)
            _showAddExhibitionDialog.value = false
            _toastMessage.value = "Событие добавлено в календарь! 🎉"
        }
    }

    fun onDeleteCustomExhibition(id: String) {
        viewModelScope.launch {
            repository.deleteCustomExhibition(id)
            if (_selectedExhibitionId.value == id) {
                _selectedExhibitionId.value = null
            }
            _toastMessage.value = "Событие удалено"
        }
    }

    fun onAddExclusiveDrop(
        exhibitionId: String,
        toyName: String,
        artistOrBrand: String,
        editionSize: String,
        estimatedPrice: String,
        dropType: String,
        isGrail: Boolean
    ) {
        viewModelScope.launch {
            val drop = ExclusiveDropItem(
                exhibitionId = exhibitionId,
                toyName = toyName,
                artistOrBrand = artistOrBrand,
                editionSize = editionSize,
                estimatedPrice = estimatedPrice,
                dropType = dropType,
                isWishlistGrail = isGrail,
                status = DropStatus.WANTED
            )
            repository.addExclusiveDrop(drop)
            _showAddDropDialog.value = false
            _toastMessage.value = "Дроп добавлен в радар! 🎯"
        }
    }

    fun onDeleteExclusiveDrop(dropId: Long) {
        viewModelScope.launch {
            repository.deleteDrop(dropId)
            _toastMessage.value = "Дроп удален из радара"
        }
    }
}

class ToyExpoViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ToyExpoViewModel::class.java)) {
            val db = ToyDatabase.getDatabase(application)
            val repo = ExhibitionRepository(db.toyDao())
            @Suppress("UNCHECKED_CAST")
            return ToyExpoViewModel(application, repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

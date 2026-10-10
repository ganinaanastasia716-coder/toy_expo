package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.dialogs.AddDropDialog
import com.example.ui.dialogs.AddExhibitionDialog
import com.example.ui.screens.DetailScreen
import com.example.ui.screens.DropsRadarScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MyTripsScreen
import com.example.ui.screens.WorldMapScreen
import com.example.ui.theme.AcidYellow
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.NeonPink
import com.example.ui.theme.ToyExpoTheme
import com.example.ui.viewmodel.NavTab
import com.example.ui.viewmodel.ToyExpoViewModel
import com.example.ui.viewmodel.ToyExpoViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ToyExpoTheme(darkTheme = true) {
                ToyExpoApp()
            }
        }
    }
}

@Composable
fun ToyExpoApp() {
    val context = LocalContext.current
    val application = context.applicationContext as android.app.Application
    val viewModel: ToyExpoViewModel = viewModel(
        factory = ToyExpoViewModelFactory(application)
    )

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.toastMessage) {
        uiState.toastMessage?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            viewModel.clearToast()
        }
    }

    if (uiState.selectedExhibition != null) {
        val selectedExpo = uiState.selectedExhibition!!
        DetailScreen(
            exhibition = selectedExpo,
            onBack = { viewModel.onClearSelectedExhibition() },
            onToggleFavorite = { viewModel.onToggleFavorite(selectedExpo) },
            onUpdateAttendance = { status -> viewModel.onUpdateAttendance(selectedExpo, status) },
            onSaveNotesAndBudget = { notes, budget, curr ->
                viewModel.onUpdateNotesAndBudget(selectedExpo, notes, budget, curr)
            },
            onToggleChecklistItem = { item -> viewModel.onToggleChecklistItem(selectedExpo, item) },
            onOpenAddDropDialog = { viewModel.setShowAddDropDialog(true) },
            onDeleteDrop = { dropId -> viewModel.onDeleteExclusiveDrop(dropId) },
            onDeleteCustomExpo = if (selectedExpo.isCustom) {
                { viewModel.onDeleteCustomExhibition(selectedExpo.id) }
            } else null
        )
    } else {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            snackbarHost = { SnackbarHost(snackbarHostState) },
            bottomBar = {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp,
                    modifier = Modifier.navigationBarsPadding()
                ) {
                    NavigationBarItem(
                        selected = uiState.currentTab == NavTab.EXPOS,
                        onClick = { viewModel.onSetTab(NavTab.EXPOS) },
                        icon = {
                            Icon(Icons.Default.DateRange, contentDescription = "Выставки")
                        },
                        label = {
                            Text(
                                text = "Выставки",
                                fontSize = 11.sp,
                                fontWeight = if (uiState.currentTab == NavTab.EXPOS) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = NeonPink,
                            selectedTextColor = NeonPink,
                            indicatorColor = NeonPink.copy(alpha = 0.2f),
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier.testTag("nav_tab_expos")
                    )

                    NavigationBarItem(
                        selected = uiState.currentTab == NavTab.DROPS_RADAR,
                        onClick = { viewModel.onSetTab(NavTab.DROPS_RADAR) },
                        icon = {
                            Icon(Icons.Default.Star, contentDescription = "Дропы")
                        },
                        label = {
                            Text(
                                text = "Дропы",
                                fontSize = 11.sp,
                                fontWeight = if (uiState.currentTab == NavTab.DROPS_RADAR) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = AcidYellow,
                            selectedTextColor = AcidYellow,
                            indicatorColor = AcidYellow.copy(alpha = 0.2f),
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier.testTag("nav_tab_drops")
                    )

                    NavigationBarItem(
                        selected = uiState.currentTab == NavTab.CITIES,
                        onClick = { viewModel.onSetTab(NavTab.CITIES) },
                        icon = {
                            Icon(Icons.Default.Place, contentDescription = "Города")
                        },
                        label = {
                            Text(
                                text = "Города",
                                fontSize = 11.sp,
                                fontWeight = if (uiState.currentTab == NavTab.CITIES) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = ElectricCyan,
                            selectedTextColor = ElectricCyan,
                            indicatorColor = ElectricCyan.copy(alpha = 0.2f),
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier.testTag("nav_tab_cities")
                    )

                    NavigationBarItem(
                        selected = uiState.currentTab == NavTab.MY_TRIPS,
                        onClick = { viewModel.onSetTab(NavTab.MY_TRIPS) },
                        icon = {
                            Icon(Icons.Default.Favorite, contentDescription = "Поездки")
                        },
                        label = {
                            Text(
                                text = "Поездки",
                                fontSize = 11.sp,
                                fontWeight = if (uiState.currentTab == NavTab.MY_TRIPS) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = NeonPink,
                            selectedTextColor = NeonPink,
                            indicatorColor = NeonPink.copy(alpha = 0.2f),
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier.testTag("nav_tab_trips")
                    )
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                AnimatedContent(
                    targetState = uiState.currentTab,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "tab_animation"
                ) { tab ->
                    when (tab) {
                        NavTab.EXPOS -> {
                            HomeScreen(
                                exhibitions = uiState.exhibitions,
                                allExhibitions = uiState.allExhibitions,
                                searchQuery = uiState.searchQuery,
                                selectedRegion = uiState.selectedRegion,
                                selectedCategory = uiState.selectedCategory,
                                onlyFavorites = uiState.onlyFavorites,
                                onlyMyTrips = uiState.onlyMyTrips,
                                onSearchChange = { viewModel.onSearchQueryChange(it) },
                                onRegionSelect = { viewModel.onRegionSelect(it) },
                                onCategorySelect = { viewModel.onCategorySelect(it) },
                                onToggleFavoritesOnly = { viewModel.onToggleFavoritesOnly() },
                                onToggleMyTripsOnly = { viewModel.onToggleMyTripsOnly() },
                                onSelectExhibition = { viewModel.onSelectExhibition(it) },
                                onToggleFavorite = { viewModel.onToggleFavorite(it) },
                                onOpenAddExpoDialog = { viewModel.setShowAddExhibitionDialog(true) }
                            )
                        }

                        NavTab.DROPS_RADAR -> {
                            BackHandler { viewModel.onSetTab(NavTab.EXPOS) }
                            DropsRadarScreen(
                                drops = uiState.grailDrops,
                                allExhibitions = uiState.allExhibitions,
                                onSelectExhibition = { viewModel.onSelectExhibition(it) },
                                onDeleteDrop = { viewModel.onDeleteExclusiveDrop(it) },
                                onOpenAddDropDialog = { viewModel.setShowAddDropDialog(true) }
                            )
                        }

                        NavTab.CITIES -> {
                            BackHandler { viewModel.onSetTab(NavTab.EXPOS) }
                            WorldMapScreen(
                                allExhibitions = uiState.allExhibitions,
                                onSelectExhibition = { viewModel.onSelectExhibition(it) }
                            )
                        }

                        NavTab.MY_TRIPS -> {
                            BackHandler { viewModel.onSetTab(NavTab.EXPOS) }
                            MyTripsScreen(
                                allExhibitions = uiState.allExhibitions,
                                onSelectExhibition = { viewModel.onSelectExhibition(it) },
                                onNavigateToFeed = { viewModel.onSetTab(NavTab.EXPOS) }
                            )
                        }
                    }
                }
            }
        }
    }

    if (uiState.showAddExhibitionDialog) {
        AddExhibitionDialog(
            onDismiss = { viewModel.setShowAddExhibitionDialog(false) },
            onConfirm = { name, edition, city, country, region, venue, startDate, endDate, category, desc, price, web, artists, brands, raffle ->
                viewModel.onAddCustomExhibition(
                    name, edition, city, country, region, venue, startDate, endDate, category, desc, price, web, artists, brands, raffle
                )
            }
        )
    }

    if (uiState.showAddDropDialog) {
        val targetExpoId = uiState.selectedExhibition?.id ?: uiState.allExhibitions.firstOrNull()?.id ?: "custom"
        AddDropDialog(
            exhibitionId = targetExpoId,
            onDismiss = { viewModel.setShowAddDropDialog(false) },
            onConfirm = { expoId, toyName, artistOrBrand, editionSize, estPrice, dropType, isGrail ->
                viewModel.onAddExclusiveDrop(
                    expoId, toyName, artistOrBrand, editionSize, estPrice, dropType, isGrail
                )
            }
        )
    }
}

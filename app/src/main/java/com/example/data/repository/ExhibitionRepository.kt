package com.example.data.repository

import com.example.data.local.ExclusiveDropEntity
import com.example.data.local.ExhibitionTrackingEntity
import com.example.data.local.ToyDao
import com.example.data.local.UserExhibitionEntity
import com.example.data.model.AttendanceStatus
import com.example.data.model.DropStatus
import com.example.data.model.ExclusiveDropItem
import com.example.data.model.Exhibition
import com.example.data.model.Region
import com.example.data.model.ToyCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class ExhibitionRepository(private val toyDao: ToyDao) {

    private val curatedExhibitions: List<Exhibition> = listOf(
        Exhibition(
            id = "dcon_2026",
            name = "DesignerCon (D-Con)",
            edition = "2026",
            city = "Анахайм, Калифорния",
            country = "США",
            region = Region.NORTH_AMERICA,
            venue = "Anaheim Convention Center, Hall D",
            startDate = "2026-11-20",
            endDate = "2026-11-22",
            displayDates = "20 – 22 ноября 2026",
            category = ToyCategory.VINYL,
            description = "Крупнейший в США конвент городского винила, поп-сюрреализма и арт-игрушек. Сотни инди-художников, кастомные выставки Dunny, вручение Designer Toy Awards.",
            ticketInfo = "Билеты в продаже: 1-Day Pass $35, Weekend $65, VIP Early Access $110",
            ticketPriceEstimate = "$35 – $110",
            officialWebsite = "https://www.designercon.com",
            featuredArtists = listOf("Ron English", "Gary Baseman", "Tara McPherson", "Mark Ryden", "Tristan Eaton"),
            featuredBrands = listOf("Kidrobot", "Superplastic", "Clutter Magazine", "3DRetro", "DKE Toys"),
            lotteryAndRaffleTips = "VIP-браслет дает вход на 2 часа раньше открытия в пятницу. Лотереи на стендах Kidrobot и Superplastic стартуют ровно в 10:00 утра.",
            lat = 33.8003,
            lng = -117.9190
        ),
        Exhibition(
            id = "wonfes_winter_2027",
            name = "Wonder Festival (WonFes Winter)",
            edition = "Зима 2027",
            city = "Тиба / Токио",
            country = "Япония",
            region = Region.ASIA,
            venue = "Makuhari Messe, Залы 1–8",
            startDate = "2027-02-07",
            endDate = "2027-02-08",
            displayDates = "7 – 8 февраля 2027",
            category = ToyCategory.SOFUBI,
            description = "Самый легендарный и масштабный мировой фестиваль гараж-китов, софуби и независимых скульпторов. Уникальная лицензия 'One-Day Copyright' позволяет авторам продавать штучные работы.",
            ticketInfo = "Входной билет ~3,000 JPY (~$20). Лотерейный Direct Pass ~6,000 JPY",
            ticketPriceEstimate = "¥3,000 – ¥6,000",
            officialWebsite = "https://wonfes.jp",
            featuredArtists = listOf("Shoko Nakazawa", "T9G", "Paul Kaiju", "Konatsu", "Izumonster"),
            featuredBrands = listOf("Kaiyodo", "Good Smile Company", "Max Factory", "Medicom Toy", "Indie Dealers"),
            lotteryAndRaffleTips = "На инди-софуби стендах очереди формируются строго по лотерейным номеркам (Ticket Lotto) до 10:30. Готовьте наличные иены!",
            lat = 35.6484,
            lng = 140.0347
        ),
        Exhibition(
            id = "pts_singapore_2026",
            name = "Pop Toy Show (PTS)",
            edition = "Сингапур 2026",
            city = "Сингапур",
            country = "Сингапур",
            region = Region.ASIA,
            venue = "Sands Expo & Convention Centre, Marina Bay Sands",
            startDate = "2026-11-13",
            endDate = "2026-11-15",
            displayDates = "13 – 15 ноября 2026",
            category = ToyCategory.BLIND_BOX,
            description = "Флагманский азиатский фестиваль арт-игрушек и блайндбоксов от POP MART. Огромные фотозоны, живые автограф-сессии с авторами Labubu, Skullpanda и Dimoo, эксклюзивные тиражи Mega Collection 1000%.",
            ticketInfo = "Стандартный билет 32 SGD. VIP 3-Day Pass с гарантией лотереи 168 SGD",
            ticketPriceEstimate = "32 – 168 SGD (~$25 – $125)",
            officialWebsite = "https://poptoyshow.sg",
            featuredArtists = listOf("Kasing Lung (Labubu)", "Kenny Wong (Molly)", "Skullpanda", "Ayan Deng (Dimoo)", "Lang (Hirono)"),
            featuredBrands = listOf("POP MART", "52TOYS", "Finding Unicorn", "TNT Space", "Mighty Jaxx"),
            lotteryAndRaffleTips = "Лотерея на эксклюзивные фигурки Mega Space Molly и Skullpanda проходит в приложении POP MART за 10 дней до открытия выставки.",
            lat = 1.2834,
            lng = 103.8607
        ),
        Exhibition(
            id = "sts_shanghai_2027",
            name = "Shanghai Toy Show (STS)",
            edition = "2027",
            city = "Шанхай",
            country = "Китай",
            region = Region.ASIA,
            venue = "Shanghai World Expo Exhibition & Convention Center",
            startDate = "2027-04-16",
            endDate = "2027-04-18",
            displayDates = "16 – 18 апреля 2027",
            category = ToyCategory.VINYL,
            description = "Главная выставка дизайнерских игрушек в континентальном Китае (более 20 000 кв.м). Эпицентр мировых арт-релизов, коллабораций и гигантских виниловых скульптур.",
            ticketInfo = "Билеты по предварительной регистрации в WeChat Mini-program (120 CNY / ~$17)",
            ticketPriceEstimate = "¥120 CNY (~$17)",
            officialWebsite = "https://www.popmart.com",
            featuredArtists = listOf("Michael Lau", "Eric So", "Pucky", "Coarse", "Chino Lam", "Seulgie"),
            featuredBrands = listOf("POP MART", "Medicom Toy", "Soap Studio", "Black Seed", "Finding Unicorn"),
            lotteryAndRaffleTips = "Необходим аккаунт WeChat с привязанным паспортом для участия в онлайновых розыгрышах дропов. Очередь на автограф-сессии строго по времени.",
            lat = 31.1884,
            lng = 121.4947
        ),
        Exhibition(
            id = "tte_bangkok_2027",
            name = "Thailand Toy Expo (TTE)",
            edition = "Бангкок 2027",
            city = "Бангкок",
            country = "Таиланд",
            region = Region.ASIA,
            venue = "CentralWorld (Зоны Eden, Dazzle, Beacon, Square A)",
            startDate = "2027-04-22",
            endDate = "2027-04-25",
            displayDates = "22 – 25 апреля 2027",
            category = ToyCategory.RESIN,
            description = "Уникальная выставка с БЕСПЛАТНЫМ входом в крупнейшем торговом молле Бангкока. Более 300 международных авторов, гигантские арт-инсталляции и культовые дропы Coarse и JPX.",
            ticketInfo = "Вход свободный (Free Entry)! Регистрация в приложении TTE для лотерей",
            ticketPriceEstimate = "Бесплатно (Free)",
            officialWebsite = "https://www.thailandtoyexpo.com",
            featuredArtists = listOf("Coarse (Mark & Sven)", "Fools Paradise", "Quiccs (TEQ63)", "Greenie & Elfie", "Chino Lam"),
            featuredBrands = listOf("Coarse", "Fools Paradise", "JPX Toys", "Mighty Jaxx", "INSTINCTOY"),
            lotteryAndRaffleTips = "Приходите к площади CentralWorld к 6:00 утра для получения карточек очереди на самые востребованные дропы Coarse и Fools Paradise.",
            lat = 13.7466,
            lng = 100.5393
        ),
        Exhibition(
            id = "ttf_taipei_2026",
            name = "Taipei Toy Festival (TTF)",
            edition = "Тайбэй 2026",
            city = "Тайбэй",
            country = "Тайвань",
            region = Region.ASIA,
            venue = "Huashan 1914 Creative Park",
            startDate = "2026-10-09",
            endDate = "2026-10-12",
            displayDates = "9 – 12 октября 2026",
            category = ToyCategory.SOFUBI,
            description = "Историческая выставка в живописном креативном парке Хуашань. Родина классического азиатского софуби, кайдзю-дизайна и тайваньской независимой иллюстрации.",
            ticketInfo = "Билет 350 TWD (~$11). Продажи на ibon и при входе",
            ticketPriceEstimate = "350 TWD (~$11)",
            officialWebsite = "https://www.taipeitoyfestival.com",
            featuredArtists = listOf("Shoko Nakazawa (Byron)", "T9G (Rangeas)", "Kasing Lung", "INSTINCTOY", "Milkboy Toys"),
            featuredBrands = listOf("Monster Taipei", "How2work", "Medicom Toy", "Black Seed Toys"),
            lotteryAndRaffleTips = "Многие японские мастера софуби разыгрывают право покупки через бумажные купоны прямо на стенде в полдень.",
            lat = 25.0440,
            lng = 121.5290
        ),
        Exhibition(
            id = "five_points_2027",
            name = "Five Points Festival",
            edition = "Бруклин 2027",
            city = "Бруклин, Нью-Йорк",
            country = "США",
            region = Region.NORTH_AMERICA,
            venue = "ZeroSpace Brooklyn",
            startDate = "2027-05-29",
            endDate = "2027-05-30",
            displayDates = "29 – 30 мая 2027",
            category = ToyCategory.CUSTOM,
            description = "Праздник уличной культуры, винила, граффити и крафтового пива. Здесь чествуют победителей Designer Toy Awards (DTA) и собираются лучшие кастомайзеры Америки.",
            ticketInfo = "General Admission $45, VIP Ticket с подарком $130",
            ticketPriceEstimate = "$45 – $130",
            officialWebsite = "https://www.fivepointsfest.com",
            featuredArtists = listOf("Czee13", "Quiccs", "Ron English", "Rsin", "Clutter Studios Roster"),
            featuredBrands = listOf("Clutter Studios", "Tenacious Toys", "Martian Toys", "Kidrobot"),
            lotteryAndRaffleTips = "VIP-билет включает эксклюзивную коллекционную фигурку фестиваля и доступ на церемонию вручения DTA в субботу вечером.",
            lat = 40.6782,
            lng = -73.9880
        ),
        Exhibition(
            id = "toy_art_uk_2026",
            name = "Toy Art UK",
            edition = "2026",
            city = "Лидс / Лондон",
            country = "Великобритания",
            region = Region.EUROPE,
            venue = "Leeds City Museum",
            startDate = "2026-10-03",
            endDate = "2026-10-04",
            displayDates = "3 – 4 октября 2026",
            category = ToyCategory.RESIN,
            description = "Главная европейская витрина инди-тоймейкеров, скульпторов по смоле, плюшевого арта и платформенных кастомов.",
            ticketInfo = "Билеты от £12. Семейные и студенческие скидки",
            ticketPriceEstimate = "£12 – £25",
            officialWebsite = "https://www.toyart.co.uk",
            featuredArtists = listOf("Pete Fowler", "Doktor A", "Czee13", "Uncle Absinthe", "Muffinman"),
            featuredBrands = listOf("Toy Art Collective", "Pobber Europe", "Unbox Industries"),
            lotteryAndRaffleTips = "Розыгрыш кастомных фигурок 'Toy Art UK Charity Raffle' проходит в 15:00 каждый день фестиваля.",
            lat = 53.8013,
            lng = -1.5486
        ),
        Exhibition(
            id = "tokyo_comic_con_2026",
            name = "Tokyo Comic Con & Sofubi Alley",
            edition = "Токио 2026",
            city = "Токио / Тиба",
            country = "Япония",
            region = Region.ASIA,
            venue = "Makuhari Messe",
            startDate = "2026-12-04",
            endDate = "2026-12-06",
            displayDates = "4 – 6 декабря 2026",
            category = ToyCategory.SOFUBI,
            description = "Японский конвент поп-культуры с мощнейшей зоной софуби и кайдзю (Sofubi Alley). Эксклюзивные Be@rbrick от Medicom Toy, Secret Base и винтажный японский винил.",
            ticketInfo = "Билет на 1 день ~4,400 JPY (~$30). Продажи через Lawson Ticket",
            ticketPriceEstimate = "¥4,400 (~$30)",
            officialWebsite = "https://tokyocomiccon.jp",
            featuredArtists = listOf("Secret Base", "Marusan", "RealxHead", "Goccodo", "Bullmark"),
            featuredBrands = listOf("Medicom Toy", "Prime 1 Studio", "Marusan", "Bandai Spirits"),
            lotteryAndRaffleTips = "Билеты в лотерейную зону Medicom Toy разыгрываются через электронную лотерею за 3 недели до конвента.",
            lat = 35.6484,
            lng = 140.0347
        ),
        Exhibition(
            id = "art_toy_culture_seoul_2027",
            name = "Art Toy Culture (ATC)",
            edition = "Сеул 2027",
            city = "Сеул",
            country = "Южная Корея",
            region = Region.ASIA,
            venue = "COEX Exhibition Hall B",
            startDate = "2027-05-06",
            endDate = "2027-05-09",
            displayDates = "6 – 9 мая 2027",
            category = ToyCategory.RESIN,
            description = "Корейский фестиваль арт-игрушек и авторской пластики. Известен утонченным минималистичным дизайном, скульптурами из полирезина и арт-коллаборациями.",
            ticketInfo = "Билеты 15,000 KRW (~$12). Продажа через Interpark",
            ticketPriceEstimate = "₩15,000 (~$12)",
            officialWebsite = "http://arttoyculture.com",
            featuredArtists = listOf("Coolrain", "Sticky Monster Lab (SML)", "Superfiction", "Grafflex", "Hands In Factory"),
            featuredBrands = listOf("CoolrainLAB", "SML", "Momot", "Superfiction Store"),
            lotteryAndRaffleTips = "Ограниченные тиражи Coolrain и SML распродаются в первые 30 минут каждого дня. Рекомендуется приходить за 1 час до открытия COEX.",
            lat = 37.5116,
            lng = 127.0592
        ),
        Exhibition(
            id = "complexcon_2026",
            name = "ComplexCon",
            edition = "Лонг-Бич 2026",
            city = "Лонг-Бич, Калифорния",
            country = "США",
            region = Region.NORTH_AMERICA,
            venue = "Long Beach Convention Center",
            startDate = "2026-11-14",
            endDate = "2026-11-15",
            displayDates = "14 – 15 ноября 2026",
            category = ToyCategory.VINYL,
            description = "Мекка хайпбист-культуры, стритвира и поп-арта. Самые желанные и дорогие дропы виниловых фигурок в коллаборациях с Такаси Мураками, KAWS и Верди.",
            ticketInfo = "General Admission $95, VIP Pass $350 (с ранним входом)",
            ticketPriceEstimate = "$95 – $350",
            officialWebsite = "https://www.complexcon.com",
            featuredArtists = listOf("Takashi Murakami", "KAWS", "Verdy", "Daniel Arsham", "Futura"),
            featuredBrands = listOf("Medicom Toy (Be@rbrick)", "Kaikai Kiki", "Complex", "Human Made"),
            lotteryAndRaffleTips = "Для участия в дропах Be@rbrick и Kaikai Kiki необходимо скачать приложение ComplexCon App и включить Bluetooth-маячки внутри зала.",
            lat = 33.7653,
            lng = -118.1892
        ),
        Exhibition(
            id = "toy_soul_hk_2026",
            name = "Hong Kong Toy Soul",
            edition = "Гонконг 2026",
            city = "Гонконг",
            country = "Гонконг",
            region = Region.ASIA,
            venue = "Star Hall, KITEC Kowloon Bay",
            startDate = "2026-12-18",
            endDate = "2026-12-20",
            displayDates = "18 – 20 декабря 2026",
            category = ToyCategory.VINYL,
            description = "Гонконгская традиция винила и коллекционных мехов. Эксклюзивные релизы от отцов-основателей гонконгского урбан-винила.",
            ticketInfo = "Билет 50 HKD (~$6.50). Продажа на месте в кассах KITEC",
            ticketPriceEstimate = "$50 HKD (~$6.50)",
            officialWebsite = "https://www.toysoul.hk",
            featuredArtists = listOf("Winson Ma", "Kenny Wong", "Eric So", "Paul Leung"),
            featuredBrands = listOf("Threezero", "Hot Toys", "Soap Studio", "How2work", "TOY0+"),
            lotteryAndRaffleTips = "Посетите стенд How2work в первую очередь — там разыгрываются оригинальные принты и виниловые миниатюры.",
            lat = 22.3242,
            lng = 114.2056
        )
    )

    private val curatedDrops: List<ExclusiveDropItem> = listOf(
        ExclusiveDropItem(
            id = 101L,
            exhibitionId = "pts_singapore_2026",
            toyName = "Mega Space Molly 1000% Cyber Neon Edition",
            artistOrBrand = "POP MART x Kenny Wong",
            editionSize = "Лимит: 200 экз.",
            estimatedPrice = "$980",
            dropType = "Лотерея в приложении PTS",
            isWishlistGrail = true,
            status = DropStatus.WANTED
        ),
        ExclusiveDropItem(
            id = 102L,
            exhibitionId = "pts_singapore_2026",
            toyName = "Labubu The Monsters Vinyl Art Sculpture",
            artistOrBrand = "Kasing Lung x How2work",
            editionSize = "Лимит: 150 экз.",
            estimatedPrice = "$280",
            dropType = "Живая очередь по браслетам",
            isWishlistGrail = true,
            status = DropStatus.WANTED
        ),
        ExclusiveDropItem(
            id = 103L,
            exhibitionId = "wonfes_winter_2027",
            toyName = "Byron x Rangeas Galactic Sofubi Duo",
            artistOrBrand = "Shoko Nakazawa & T9G",
            editionSize = "Лимит: 50 наборов",
            estimatedPrice = "¥28,000 (~$185)",
            dropType = "Lotto Gacha на стенде",
            isWishlistGrail = true,
            status = DropStatus.WANTED
        ),
        ExclusiveDropItem(
            id = 104L,
            exhibitionId = "dcon_2026",
            toyName = "Grin Vinyl Figure 12\" (Metallic Chrome Pink)",
            artistOrBrand = "Ron English x Popaganda",
            editionSize = "Лимит: 100 экз.",
            estimatedPrice = "$220",
            dropType = "VIP Early Access лотерея",
            isWishlistGrail = false,
            status = DropStatus.WANTED
        ),
        ExclusiveDropItem(
            id = 105L,
            exhibitionId = "tte_bangkok_2027",
            toyName = "Coarse Noop & Paw: Bangkok Sunset Glow",
            artistOrBrand = "Coarse Toys (Mark & Sven)",
            editionSize = "Лимит: 250 экз.",
            estimatedPrice = "8,500 THB (~$230)",
            dropType = "Очередь Eden Zone Square",
            isWishlistGrail = true,
            status = DropStatus.WANTED
        ),
        ExclusiveDropItem(
            id = 106L,
            exhibitionId = "five_points_2027",
            toyName = "Canbot 5oz Brooklyn Street Rust Edition",
            artistOrBrand = "Czee13 x Clutter",
            editionSize = "Лимит: 75 экз.",
            estimatedPrice = "$95",
            dropType = "Стенд Clutter Studios",
            isWishlistGrail = false,
            status = DropStatus.WANTED
        )
    )

    fun getAllExhibitions(): Flow<List<Exhibition>> {
        return combine(
            toyDao.getAllUserExhibitions(),
            toyDao.getAllTrackings(),
            toyDao.getAllDrops()
        ) { userEntities, trackings, drops ->
            val trackingMap = trackings.associateBy { it.exhibitionId }
            val dropsMap = drops.groupBy { it.exhibitionId }

            val customList = userEntities.map { entity ->
                val track = trackingMap[entity.id]
                val itemDrops = dropsMap[entity.id].orEmpty().map { it.toModel() }
                Exhibition(
                    id = entity.id,
                    name = entity.name,
                    edition = entity.edition,
                    city = entity.city,
                    country = entity.country,
                    region = try { Region.valueOf(entity.regionStr) } catch (_: Exception) { Region.ALL },
                    venue = entity.venue,
                    startDate = entity.startDate,
                    endDate = entity.endDate,
                    displayDates = entity.displayDates,
                    category = try { ToyCategory.valueOf(entity.categoryStr) } catch (_: Exception) { ToyCategory.ALL },
                    description = entity.description,
                    ticketInfo = entity.ticketInfo,
                    ticketPriceEstimate = entity.ticketPriceEstimate,
                    officialWebsite = entity.officialWebsite,
                    featuredArtists = entity.featuredArtistsStr.split(",").map { it.trim() }.filter { it.isNotEmpty() },
                    featuredBrands = entity.featuredBrandsStr.split(",").map { it.trim() }.filter { it.isNotEmpty() },
                    lotteryAndRaffleTips = entity.lotteryAndRaffleTips,
                    lat = entity.lat,
                    lng = entity.lng,
                    isCustom = true,
                    isFavorite = track?.isFavorite ?: false,
                    attendance = try { AttendanceStatus.valueOf(track?.attendanceStatusStr ?: "NONE") } catch (_: Exception) { AttendanceStatus.NONE },
                    userNotes = track?.userNotes.orEmpty(),
                    plannedBudget = track?.plannedBudget ?: 0.0,
                    budgetCurrency = track?.budgetCurrency ?: "USD",
                    completedChecklist = track?.completedChecklistStr?.split("|")?.filter { it.isNotEmpty() }.orEmpty(),
                    customDrops = itemDrops
                )
            }

            val curatedMapped = curatedExhibitions.map { item ->
                val track = trackingMap[item.id]
                val dbDrops = dropsMap[item.id].orEmpty().map { it.toModel() }
                val initialDrops = curatedDrops.filter { it.exhibitionId == item.id }
                val mergedDrops = if (dbDrops.isEmpty()) initialDrops else dbDrops
                item.copy(
                    isFavorite = track?.isFavorite ?: false,
                    attendance = try { AttendanceStatus.valueOf(track?.attendanceStatusStr ?: "NONE") } catch (_: Exception) { AttendanceStatus.NONE },
                    userNotes = track?.userNotes.orEmpty(),
                    plannedBudget = track?.plannedBudget ?: 0.0,
                    budgetCurrency = track?.budgetCurrency ?: "USD",
                    completedChecklist = track?.completedChecklistStr?.split("|")?.filter { it.isNotEmpty() }.orEmpty(),
                    customDrops = mergedDrops
                )
            }

            (curatedMapped + customList).sortedBy { it.startDate }
        }
    }

    fun getAllGrailDrops(): Flow<List<ExclusiveDropItem>> {
        return combine(toyDao.getAllDrops()) { dropsArray ->
            val dbDrops = dropsArray.firstOrNull().orEmpty().map { it.toModel() }
            if (dbDrops.isEmpty()) {
                curatedDrops
            } else {
                val dbExhibitionIds = dbDrops.map { it.id }.toSet()
                dbDrops + curatedDrops.filter { it.id !in dbExhibitionIds }
            }
        }
    }

    suspend fun updateTracking(
        exhibitionId: String,
        isFavorite: Boolean,
        attendance: AttendanceStatus,
        userNotes: String,
        plannedBudget: Double,
        budgetCurrency: String,
        completedChecklist: List<String>
    ) {
        toyDao.upsertTracking(
            ExhibitionTrackingEntity(
                exhibitionId = exhibitionId,
                isFavorite = isFavorite,
                attendanceStatusStr = attendance.name,
                userNotes = userNotes,
                plannedBudget = plannedBudget,
                budgetCurrency = budgetCurrency,
                completedChecklistStr = completedChecklist.joinToString("|")
            )
        )
    }

    suspend fun addCustomExhibition(exhibition: UserExhibitionEntity) {
        toyDao.insertUserExhibition(exhibition)
    }

    suspend fun deleteCustomExhibition(id: String) {
        toyDao.deleteUserExhibition(id)
    }

    suspend fun addExclusiveDrop(drop: ExclusiveDropItem) {
        toyDao.insertDrop(
            ExclusiveDropEntity(
                exhibitionId = drop.exhibitionId,
                toyName = drop.toyName,
                artistOrBrand = drop.artistOrBrand,
                editionSize = drop.editionSize,
                estimatedPrice = drop.estimatedPrice,
                dropType = drop.dropType,
                isWishlistGrail = drop.isWishlistGrail,
                statusStr = drop.status.name
            )
        )
    }

    suspend fun deleteDrop(dropId: Long) {
        toyDao.deleteDrop(dropId)
    }

    private fun ExclusiveDropEntity.toModel(): ExclusiveDropItem {
        return ExclusiveDropItem(
            id = id,
            exhibitionId = exhibitionId,
            toyName = toyName,
            artistOrBrand = artistOrBrand,
            editionSize = editionSize,
            estimatedPrice = estimatedPrice,
            dropType = dropType,
            isWishlistGrail = isWishlistGrail,
            status = try { DropStatus.valueOf(statusStr) } catch (_: Exception) { DropStatus.WANTED }
        )
    }
}

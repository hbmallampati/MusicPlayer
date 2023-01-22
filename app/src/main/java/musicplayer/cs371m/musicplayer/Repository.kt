package musicplayer.cs371m.musicplayer

// This is the model in MVVM
data class SongInfo(val name: String, val rawId: Int, val time: String)

class Repository {
    private var songResources = hashMapOf(
        "Dark Star" to
                SongInfo("Dark Star",
                    R.raw.dark_star_excerpt,
                    "1:30"),
        "What's Mine" to
                SongInfo("What's Mine",
                    R.raw.whats_mine_excerpt,
                    "1:15"),
        "La Fille Aux Cheveux De Lin" to
                SongInfo("La Fille Aux Cheveux De Lin",
                    R.raw.debussy_la_fille_aux_cheveux_de_lin,
                    "2:43"),
        "Rondo Alla Turca" to
                SongInfo("Rondo Alla Turca",
                    R.raw.rondo_alla_turca,
                    "3:02"),
        "Big Digits" to
                SongInfo("Big Digits",
                    R.raw.big_digits_excerpt,
                    "0:49"),
        "Base after base" to
                SongInfo("Base after base",
                    R.raw.base_after_base,
                    "1:25"),
        "Can't let go" to
                SongInfo("Can't let go",
                    R.raw.cant_let_go,
                    "1:24")
    )

    fun fetchData(): HashMap<String, SongInfo> {
        return songResources
    }
}
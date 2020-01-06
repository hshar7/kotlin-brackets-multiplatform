package brackets

/***************************************************/

open class Team
open class Match(val id: String, var team1: Team?, var team2: Team?, var winner: Team?, var match1: Match?, var match2: Match?)
open class Bracket(val upper: MutableList<Match> = mutableListOf(), val lower: MutableList<Match> = mutableListOf())

enum class BracketType {
    SINGLE_ELIMINATION,
    DOUBLE_ELIMINATION
}

enum class BracketSize(val value: Int) {
    S64(64),
    S32(32),
    S16(16),
    S8(8),
    S4(4),
    S2(2)
}

fun generateBracket(teams: List<Team>, type: BracketType, bracketSize: BracketSize): Bracket {
    return when(type) {
        BracketType.SINGLE_ELIMINATION -> generateSE(teams, bracketSize)
        BracketType.DOUBLE_ELIMINATION -> generateDE(teams, bracketSize)
    }
}

expect fun generateSE(teams: List<Team>, bracketSize: BracketSize): Bracket

expect fun generateDE(teams: List<Team>, bracketSize: BracketSize): Bracket

package brackets

import java.util.*

actual fun generateSE(teams: List<Team>, bracketSize: BracketSize): Bracket {
    // TODO: Have a check for amount of participants and sub in byes

    val bracket = Bracket()
    val matchesQueue: Queue<Match> = ArrayDeque<Match>()
    val participants = teams.toMutableList()

    for (i in 1..bracketSize.value - 1) {

        var match1: Match? = null
        var match2: Match? = null
        var team1: Team? = null
        var team2: Team? = null

        if (i <= bracketSize.value / 2) {
            team1 = participants[(0..participants.size - 1).random()]
            participants.remove(team1)
            team2 = participants[(0..participants.size - 1).random()]
            participants.remove(team2)
        } else {
            match1 = matchesQueue.remove()
            match2 = matchesQueue.remove()
        }

        val match = Match(
            id = UUID.randomUUID().toString(),
            team1 = team1,
            team2 = team2,
            winner = null,
            match1 = match1,
            match2 = match2
        )

        matchesQueue.add(match)
        bracket.upper.add(match)
    }

    return bracket
}

actual fun generateDE(teams: List<Team>, bracketSize: BracketSize): Bracket {
    // TODO: Have a check for amount of participants and sub in byes

    val matchesWQueue: Queue<Match> = ArrayDeque<Match>()
    val matchesLQueue: Queue<Match> = ArrayDeque<Match>()
    val backfillQ: Queue<Match> = ArrayDeque<Match>()
    val participants = teams.toMutableList()
    val bracket = Bracket()

    for (i in 1..bracketSize.value / 2) { // Seed Round
        var team1: Team? = null
        var team2: Team? = null

        if (participants.isNotEmpty()) {
            team1 = participants[(0..participants.size - 1).random()]
            participants.remove(team1)
        }
        if (participants.isNotEmpty()) {
            team2 = participants[(0..participants.size - 1).random()]
            participants.remove(team2)
        }

        val seedMatch = Match(
            id = UUID.randomUUID().toString(),
            team1 = team1,
            team2 = team2,
            winner = null,
            match1 = null,
            match2 = null
        )

        matchesWQueue += seedMatch
        matchesLQueue += seedMatch
        bracket.upper += seedMatch
    }

    while (matchesWQueue.size > 1) { // Generate upper bracket matches
        val match1 = matchesWQueue.remove()
        val match2 = matchesWQueue.remove()

        val matchW = Match(
            id = UUID.randomUUID().toString(),
            team1 = null,
            team2 = null,
            winner = null,
            match1 = match1,
            match2 = match2
        )

        matchesWQueue += matchW
        bracket.upper += matchW
        backfillQ += matchW // add match to backfill for Lower Queue
    }

    var roundSwitch = bracketSize.value / 2
    var switch = false
    var counter = 0
    var switchedCounter = 0
    while (matchesLQueue.size > 0 && backfillQ.size > 0) { // Generate losers bracket matches
        var match1: Match?
        var match2: Match?
        if (switch) {
            match1 = matchesLQueue.remove()
            match2 = backfillQ.remove()
            switchedCounter += 2
            if (switchedCounter == roundSwitch) {
                // switch back
                roundSwitch /= 2
                switch = false
                // reset counters
                switchedCounter = 0
            }
        } else {
            match1 = matchesLQueue.remove()
            match2 = matchesLQueue.remove()
            counter += 2
            if (counter == roundSwitch) {
                switch = true
                counter = 0
            }
        }

        val matchL = Match(
            id = UUID.randomUUID().toString(),
            team1 = null,
            team2 = null,
            winner = null,
            match1 = match1,
            match2 = match2
        )
        matchesLQueue += matchL
        bracket.lower += matchL
    }

    // Add final match
    bracket.lower += Match(
        id = UUID.randomUUID().toString(),
        team1 = null,
        team2 = null,
        winner = null,
        match1 = matchesWQueue.remove(),
        match2 = matchesLQueue.remove()
    )

    return bracket
}

package comp1110.ass2;

import comp1110.ass2.gui.Board;

import java.util.*;

/**
 * This class provides the text interface for the Steps Game
 * <p>
 * The game is based directly on Smart Games' IQ-Steps game
 * (http://www.smartgames.eu/en/smartgames/iq-steps)
 */
public class StepsGame {

    /**
     * Determine whether a piece placement is well-formed according to the following:
     * - it consists of exactly three characters
     * - the first character is in the range A .. H (shapes)
     * - the second character is in the range A .. H (orientations)
     * - the third character is in the range A .. Y and a .. y (locations)
     *
     * @param piecePlacement A string describing a piece placement
     * @return True if the piece placement is well-formed
     */
    static boolean isPiecePlacementWellFormed(String piecePlacement) {
        // FIXME Task 2: determine whether a piece placement is well-forme
        // Authorship details: Task2 is written by Chan Xu (u6233112).

        /* Determine whether it consists of exactly three characters.*/
        if (piecePlacement.length() == 3) {
            char[] placementChar = piecePlacement.toCharArray();
            if (placementChar[0] >= 'A' && placementChar[0] <= 'H') {                    // Check the first char
                if (placementChar[1] >= 'A' && placementChar[1] <= 'H') {                // Check the second char
                    return ((placementChar[2] >= 'A' && placementChar[2] <= 'Y') ||     // Check the third char
                            (placementChar[2] >= 'a' && placementChar[2] <= 'y'));
                }
            }
        }
        return false;
    }


    /**
     * Determine whether a placement string is well-formed:
     * - it consists of exactly N three-character piece placements (where N = 1 .. 8);
     * - each piece placement is well-formed
     * - no shape appears more than once in the placement
     *
     * @param placement A string describing a placement of one or more pieces
     * @return True if the placement is well-formed
     */
    static boolean isPlacementWellFormed(String placement) {
        // FIXME Task 3: determine whether a placement is well-formed
        // Authorship details: Task3 is written by the group.

        /* Determine whether the placement is null.*/
        if (placement == null)
        {
            return false;
            /* Determine whether the length of placement is divided by three.*/
        }else if (placement.length() % 3 != 0)
        {
            return false;
        }else
        {
            for (int i = 0; i < placement.length(); i += 3) {
                if (!isPiecePlacementWellFormed(placement.substring(i, i + 3))) {
                    return false;
                }
            }

            String[] a = new String[placement.length() / 3];

            for (int i = 0; i < placement.length() / 3; i++) {
                a[i] = placement.substring(3 * i, 3 * i + 3);
                if (isPiecePlacementWellFormed(a[i]) && NoDuplicate(placement)) {
                    return true;
                }
            }
            return false;
        }
    }


    /**
     * Helper for Task 3.
     * The idea is from StackOverflow: https://stackoverflow.com/questions/13119926/find-duplicate-characters-in-a-string-and-count-the-number-of-occurances-using-j
     *
     * @param placement A placement sequence string
     * @return False If the placements are not duplicate
     */
    static boolean NoDuplicate(String placement) {
        boolean Duplicate = true;
        char[] b = placement.toCharArray();
        for (int i = 0; i < b.length; i += 3)
            for (int k = 0; k < b.length; k += 3)
                if (i != k && b[i] == b[k])
                    Duplicate = false;
        return Duplicate;
    }


    /**
     * Determine whether a placement sequence is valid.  To be valid, the placement
     * sequence must be well-formed and each piece placement must be a valid placement
     * (with the pieces ordered according to the order in which they are played).
     *
     * @param placement A placement sequence string
     * @return True if the placement sequence is valid
     */
   public static boolean isPlacementSequenceValid(String placement) {
        // FIXME Task 5: determine whether a placement sequence is valid
        // Authorship details: Task5 is written by Chan Xu (u6233112).

        /* Determine whether the placement is well formed.*/
        if (!isPlacementWellFormed(placement))
        {
            return false;
        } else
        {
            /* Create a board.*/
            GameBoard brd = new GameBoard();

            for (int i = 0; i < placement.length(); i += 3)
            {
                String pieceState = placement.substring(i, i+2);        // separate into pieces string
                Piece pce = new Piece(pieceState);                     // create the piece with its shape information by using number 0,1,2
                char pos = placement.charAt(i+2);                      // get piece position

                /* Determine whether the piece can be placed on the board.*/
                if (!brd.isPiecePlaced(pce, pos))
                {
                    return false;
                }
            }
            return true;
        }
    }


    /**
     * Given a string describing a placement of pieces and a string describing
     * an (unordered) objective, return a set of all possible next viable
     * piece placements.   A viable piece placement must be a piece that is
     * not already placed (ie not in the placement string), and which will not
     * obstruct any other unplaced piece.
     *
     * @param placement A valid sequence of piece placements where each piece placement is drawn from the objective
     * @param objective A valid game objective, but not necessarily a valid placement string
     * @return An set of viable piece placements
     */
    static Set<String> getViablePiecePlacements(String placement, String objective) {
        // FIXME Task 6: determine the correct order of piece placements
        // Authorship details: Task6 is written by Chan Xu (u6233112).

        /* Create a box storing objective string which is different from placement string.*/
        Set<String> box = new HashSet<>();

        for (int i = 0; i < objective.length(); i+=3)
        {
            box.add(objective.substring(i, i+3));
        }
        for (int i = 0; i < placement.length(); i+=3)
        {
            box.remove(placement.substring(i, i+3));
        }

        /* To get viable pieces.*/
        Set<String> viablePieces = new HashSet<>();

        for (String obj: box)
        {
            if (isViablePiece(obj, box))
            {
                viablePieces.add(obj);
            }
        }
        return viablePieces;
    }


    /**
     * Helper for Task 6.
     * @param obj
     * @param box
     * @return True if A viable piece placement will not obstruct any other unplaced piece.
     */
    private static boolean isViablePiece(String obj, Set<String> box)
    {
        for (String sub: box)
        {
            if (!obj.equals(sub))
            {
                if (!isPlacementSequenceValid(obj+sub))
                {
                    return false;
                }
            }
        }
        return true;
    }


    /**
         * Return an array of all unique (unordered) solutions to the game, given a
         * starting placement.   A given unique solution may have more than one than
         * one placement sequence, however, only a single (unordered) solution should
         * be returned for each such case.
         *
         * @param placement  A valid piece placement string.
         * @return An array of strings, each describing a unique unordered solution to
         * the game given the starting point provided by placement.
         */
        static String[] getSolutions(String placement) {
            // FIXME Task 9: determine all solutions to the game, given a particular starting placement
            return null;
        }
    }

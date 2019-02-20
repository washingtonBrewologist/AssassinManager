import java.util.*;
/**<h1>Class: Assassin Manager</h1>
 *     <h2>Author: Nick Williams</h2>
 *     <h3>Instructor: Ryan Parsons</h3>
 *     <h3>Date: 2/20/2019</h3>
 * <p> Class 'AssassinManager' keeps track of
 *     who is stalking who within the killRing.
 *     The killRing contains people still in the
 *     game, while the graveYard contains those
 *     who have been killed.
 * </p>
 */

public class AssassinManager {

    private AssassinNode killRingFront;
    private AssassinNode graveYardFront;

    /**
     * Instantiates a new Assassin manager.
     * Builds killRing nodes.
     * @param names the names of people who are being added
     *              to the killRing.
     * @exception IllegalArgumentException if names list is null
     *              or if the size of the list is 0.
     */
    public AssassinManager(List<String> names){
        if (names == null || names.size() == 0){
            throw new IllegalArgumentException("");
        }
        for (int i = 0; i < names.size(); i++){
            String name = names.get(i);
            AssassinNode assassin = new AssassinNode(name);
            if (killRingFront == null){
                killRingFront = assassin;
            } else {
                AssassinNode current = killRingFront;
                while (current.next != null){
                    current = current.next;
                }
                current.next = assassin;
            }
        }
    }

    /**
     * Prints the current killRing stalking sequence.
     * Last step refers back to front of killRing to
     * simulate a circular list.
     */
    public void printKillRing() {
        AssassinNode current = killRingFront;
        while (current.next != null){
            System.out.println("  " + current.name
                    + " is stalking " + current.next.name);
            current = current.next;
        }
        // this line will give the illusion of a circular list
        System.out.println("  " + current.name
                + " is stalking " + killRingFront.name);
    }

    /**
     * Prints the nodes that have been killed and
     * placed into the graveyard. Also displays who
     * the node was killed by.
     */
    public void printGraveyard(){
        AssassinNode current = graveYardFront;
        while (current != null){
            if (current.next == null){
                System.out.println("  " + current.name
                        + " was killed by " + current.killer);
            } else {
                System.out.println("  " + current.name
                        + " was killed by " + current.killer);
            }
            current = current.next;
        }
        System.out.println();
    }

    /**
     * Used to check if a person is currently in either
     * the killRing or the graveYard.
     * @param name    the name of the node in question
     * @param current used to gain access to front of GY and KR
     * @return boolean. If node exists return true, else returns false.
     */
    public boolean verifyExistence(String name, AssassinNode current){
        while (current != null){
            if (current.name.equalsIgnoreCase(name)){
                return true;
            }
            current = current.next;
        }
        return false;
    }

    /**
     * Returns true or false depending on if game is still
     * being played.
     * @return only one node left in KR, returns true. Else,
     * game is still being played.
     */
    public boolean isGameOver(){
        return killRingFront.next == null;
    }

    /**
     * Will return the name of the last standing person
     * in the killRing.
     * @return A string representing the winning nodes name.
     * if the game isn't over will return null.
     */
    public String winner(){
        if (!isGameOver()){
            return null;
        }
        return killRingFront.name;
    }

    /**
     * Allows for the checking of names currently in
     * the graveyard.
     * @param name the name of the node in question.
     * @return true if the node exists, false otherwise.
     */
    public boolean graveyardContains(String name){
        AssassinNode current = graveYardFront;
        return verifyExistence(name, current);
    }

    /**
     * Allows for the checking of names currently in
     * the killRing.
     * @param name the name of the node in question
     * @return true if the node exists, false otherwise.
     */
    public boolean killRingContains(String name){
        AssassinNode current = killRingFront;
        return verifyExistence(name, current);
    }

    /**
     * Handles keeping track of the killers name and the
     * node they killed. Places the dead node into the
     * graveyard.
     * @param deadNode The node who has been killed.
     * @param killer   The node responsible for the killing.
     */
    public void cemeteryGates(AssassinNode deadNode, AssassinNode killer){
        deadNode.killer = killer.name;
        deadNode.next = graveYardFront;
        graveYardFront = deadNode;
    }

    /**
     * Locates the kill node within the killRing.
     * @param name The name of the dead node, used to
     *             locate the killNode position.
     * @return the node containing the killer.
     */
    public AssassinNode logKillNode(String name){
        AssassinNode assassin = killRingFront;
        // This test will ensure that if the first person in the kill ring
        // is the victim, the loop will return the last name in the kill ring.
        while (assassin != null && assassin.next != null
                && (!assassin.next.name.equalsIgnoreCase(name))){
            assassin = assassin.next;
        }
        return assassin;
    }

    /**
     * Handles the recording of the killings and the flow
     * of placing dead nodes into the graveyard.
     * @param name the name of the dead node.
     * @exception IllegalStateException if the game is over.
     */
    public void kill(String name){
        if (isGameOver()){
            throw new IllegalStateException("Game is over!");
        }
        // Node that references the killer
        AssassinNode theAssassin = logKillNode(name);
        if (killRingFront.name.equalsIgnoreCase(name)){

            // Set deadNode to kill ring front
            AssassinNode deadNode = killRingFront;

            // front of the kill ring moves to the next in line
            killRingFront = deadNode.next;

            // move dead node into cemetery and record the
            // name of the assassin.
            cemeteryGates(deadNode, theAssassin);
        } else {
            // because we know the next victims name,
            // our killer will always be the person who
            // is stalking them.
            AssassinNode deadNode = theAssassin.next;
            theAssassin.next = deadNode.next;
            cemeteryGates(deadNode, theAssassin);
        }
    }
}

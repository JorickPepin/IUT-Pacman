
package ghosts;

import iut.BoxGameItem;
import iut.GameItem;
import java.util.List;
import pacman.Map;
import pacman.PacMan;

/**
 * Classe abstraite représentant un fantôme
 * Chaque fantôme possède des caractéristiques différentes (apparence, déplacements, ...)
 * @author Jorick
 */
public abstract class Ghost extends BoxGameItem {
    
    private boolean isVulnerable = false;
    
    private boolean isVulnerableGhostBlue = true;
    
    private boolean isDie = false;
    
    private final PacMan game;
    
    private final Map map;
    
    public Ghost(PacMan game, String name, int x, int y) {
        super(game, name, x, y); 
        
        this.game = game;
        
        this.map = PacMan.getMap();
    }
    
  
    
    @Override
    public void collideEffect(GameItem gi) {}

    @Override
    public String getItemType() {
        return "ghost";
    }
 
    @Override
    public void evolve(long l) {}
    
    /**
     * Méthode permettant de devenir le fantôme comme étant vulnérable
     * (pacman a mangé une super pac-gomme (gros point))
     * @param vulnerable = true ou false selon s'il est vulnérable ou pas
     */
    public void becomeVulnerable(boolean vulnerable) {
        this.isVulnerable = vulnerable; 
        
        // l'image du fantôme devient celle du fantôme bleu
        this.changeSprite("images/Ghosts/dangerBlue");
        
        // on initialise le compteur permettant de définir la durée pendant
        // laquelle le fantôme va être vulnérable à 0
        this.setTime(0);
    }
    
    public boolean isVulnerable() {
        return this.isVulnerable;
    }
    
    /**
     * Méthode permettant d'alterner entre l'image du fantôme en bleu et 
     * en blanc lorsqu'il n'est presque plus vulnérable
     */
    public void changeSpriteVulnerableGhost() {
        if (!isVulnerableGhostBlue) {
            changeSprite("images/Ghosts/dangerBlue");
            this.isVulnerableGhostBlue = true;
        } else {
            super.changeSprite("images/Ghosts/dangerWhite");
            isVulnerableGhostBlue = false;
        }
    }
  
    /**
     * Méthode permettant de gérer le temps pendant lequel le fantôme
     * est vulnérable
     * @param time = le temps qui s'est écoulé depuis qu'il est vulnérable
     */
    protected void setTheVunerableGhost(int time) {

        // si le compteur est supérieur à 30, on fait clignoter 
        // le fantôme pour avertir le joueur que c'est bientôt fini
        if (time > 30) {
            this.changeSpriteVulnerableGhost();
        }
        // si le compteur est égal à 40 (~ 10sec), le fantôme
        // n'est plus vulnérable
        if (time == 40) {
            this.becomeVulnerable(false);
            this.setTime(0);
        }
    }
    
    protected void returnToOrigne(Ghost g) {
        int origine = 0;
       
        // suivant le fantôme, l'origine n'est pas la même
        switch (g.getGhostName()) {
            
            case "Clyde":
                origine = map.getSquareToNode().get(map.getSquares()[8][13]);
                break;
        }

        List<Integer> chemin = map.getGraph().shortestPath(map.getSquareToNode().get(map.getSquares()[g.getI()][g.getJ()]), origine);

        g.getPosition().setX(map.getNodeToSquare().get(chemin.get(1)).getJ() * 28);
        g.getPosition().setY(map.getNodeToSquare().get(chemin.get(1)).getI() * 28);

        g.setI(map.getNodeToSquare().get(chemin.get(1)).getI());
        g.setJ(map.getNodeToSquare().get(chemin.get(1)).getJ());
    }

    public void die() {
        this.isDie = true;
    }
    
    public void live() {
        this.isDie = false;
    }
    
    public boolean isDead() {
        return this.isDie;
    }
    
    public abstract int getI();
    public abstract int getJ();
    
    public abstract void setI(int i);
    public abstract void setJ(int j);
    
    public abstract void initGhost();
    
    public abstract void setTime(int time);
    
    public abstract String getGhostName();
}
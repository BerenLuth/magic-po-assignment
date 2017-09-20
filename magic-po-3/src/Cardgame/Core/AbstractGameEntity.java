
package Cardgame.Core;


public abstract class AbstractGameEntity extends Observable implements GameEntity {
    protected boolean isRemoved=false;
    public boolean isRemoved() { return isRemoved; }
    public void remove() { isRemoved=true; }

    //targeting management
    private boolean possibleTarget;
    private int targetIndex;


    @Override
    public boolean isPossibleTarget() {
        return possibleTarget;
    }

    @Override
    public int getTargetIndex() {
        return targetIndex;
    }

    @Override
    public void setTargetIndex(int i) {
        targetIndex = i;
    }

    @Override
    public void setPossibleTarget() {
        System.out.println(this.getClass() + ": selezionabile come target");
        possibleTarget = true;
    }

    @Override
    public void endTargeting() {
        System.out.println(this.getClass() + ": non più selezionabile");
        possibleTarget = false;
    }
}

package asteroids;

import javafx.scene.shape.Polygon;

public class Projectile extends Character {

    private Boolean collided;

    public Projectile(int x, int y){
        super(new Polygon(2, -2, 2, 2, -2, 2, -2, -2), x, y);
        this.collided=false;

    }

    @Override
    public void move() {
        super.character.setTranslateX(this.character.getTranslateX() + super.movement.getX());
        this.character.setTranslateY(this.character.getTranslateY() + super.movement.getY());
        if(super.character.getTranslateX()<0){
            super.setAlive(false);
        }
        if(super.character.getTranslateX()>Base.WIDTH){
            super.setAlive(false);
        }
        if(super.character.getTranslateY()<0){
            super.setAlive(false);
        }
        if(super.character.getTranslateY()>Base.HEIGHT){
            super.setAlive(false);
        }
    }

    public Boolean isCollided(){
        return this.collided;
    }

    public void setCollided(Boolean state){
        this.collided=state;
    }

//    public boolean offScreen(){
//        if((super.getCharacter().getTranslateX()>Base.WIDTH || this.getCharacter().getTranslateY()<0) || (this.getCharacter().getTranslateY()>Base.HEIGHT || this.getCharacter().getTranslateY()<0)){
//            return true;
//        }
//        return false;
//    }
//
//    @Override
//    public void move(){
//        this.getCharacter().setTranslateX(this.getCharacter().getTranslateX() + this.getMovement().getX());
//        this.getCharacter().setTranslateY(this.getCharacter().getTranslateY() + this.getMovement().getY());
//
//    }


}

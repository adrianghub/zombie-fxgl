package com.adrianghub.zombie.components;

import com.adrianghub.zombie.ZombieApp;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;

public class ShotgunComponent extends Component {
    @Override
    public void onUpdate(double tpf) {
        rotate(tpf);
        followPlayer(tpf);
    }

    private void rotate(double tpf) {
        getEntity().rotateBy(180 * tpf * 0.5);

        if (getEntity().getRotation() >= 360) {
            getEntity().setRotation(0);
        }
    }

    private void followPlayer(double tpf) {
        Entity player = getEntity().getWorld().getSingleton(ZombieApp.EntityType.SURVIVOR);
        if (getEntity().distance(player) < 50) {
            getEntity().translateTowards(player.getCenter(), 50 * tpf);
        }
    }
}

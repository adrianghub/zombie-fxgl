package com.adrianghub.zombie.components;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;

import static com.adrianghub.zombie.ZombieApp.EntityType.SURVIVOR;

public class CoinComponent extends Component {
    @Override
    public void onUpdate(double tpf) {
        rotate(tpf);
        followPlayer(tpf);
    }

    private void rotate(double tpf) {
        getEntity().rotateBy(180 * tpf);

        if (getEntity().getRotation() >= 360) {
            getEntity().setRotation(0);
        }
    }

    private void followPlayer(double tpf) {
        Entity player = getEntity().getWorld().getSingleton(SURVIVOR);
        if (getEntity().distance(player) < 100) {
            getEntity().translateTowards(player.getCenter(), 200 * tpf);
        }
    }
}

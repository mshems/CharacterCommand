package core.items.consumable;

import core.character.PlayerCharacter;
import core.items.Item;

public class Consumable extends Item {
    private ConsumableEffect consumableEffect;

    public Consumable(String name, String... tags) {
        super(name, tags);
        addTag("consumable");
        addTag(".TYPE:CONSUMABLE");
    }

    public Consumable(String name, ConsumableEffect effect, String... tags) {
        this(name, tags);
        consumableEffect = effect;
    }

    @Override
    public void use(PlayerCharacter pc){
        if(consumableEffect!=null){
            consumableEffect.doEffect(pc);
        }
    }
}

package de.maxhenkel.plane.events;

import de.maxhenkel.plane.entity.EntityPlaneDamageBase;
import de.maxhenkel.plane.sound.ModSounds;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import de.maxhenkel.plane.item.ModItems;

public class InteractEvents {

    @SubscribeEvent
    public void onInteract(PlayerInteractEvent.EntityInteract evt) {
        Player player = evt.getEntity();
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (!stack.getItem().equals(ModItems.WRENCH.get())) {
            stack = player.getItemInHand(InteractionHand.OFF_HAND);
            if (!stack.getItem().equals(ModItems.WRENCH.get())) {
                return;
            }
        }

        EntityPlaneDamageBase plane = null;

        if (!(evt.getTarget() instanceof EntityPlaneDamageBase)) {
            return;
        }

        if (evt.getTarget() instanceof EntityPlaneDamageBase) {
            plane = (EntityPlaneDamageBase) evt.getTarget();
        }

        if (plane == null) {
            return;
        }

        evt.setCanceled(true);

        float damage = plane.getPlaneDamage();

        if (damage <= 0F) {
            return;
        }

        if (player instanceof ServerPlayer serverPlayer) {
            stack.hurtAndBreak(1, serverPlayer.serverLevel(), serverPlayer, (item) -> {
            });
        }

        plane.setPlaneDamage(Math.max(damage - 1F, 0F));

        float newDamage = plane.getPlaneDamage();

        if (Math.round(newDamage) % 10 == 0) {
            ModSounds.playSound(ModSounds.RATCHET.get(), evt.getLevel(), plane.blockPosition(), null, SoundSource.NEUTRAL, 1F);
        }
    }

}

/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module

import Xigua.Disabler.OtherDisabler
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.KeyEvent
import net.ccbluex.liquidbounce.event.Listenable
import net.ccbluex.liquidbounce.features.module.modules.combat.*
import net.ccbluex.liquidbounce.features.module.modules.exploit.*
import net.ccbluex.liquidbounce.features.module.modules.gui.Blur
import net.ccbluex.liquidbounce.features.module.modules.gui.GodLightSync
import net.ccbluex.liquidbounce.features.module.modules.gui.HudDesigner
import net.ccbluex.liquidbounce.features.module.modules.gui.Title
import net.ccbluex.liquidbounce.features.module.modules.hyt.*
import net.ccbluex.liquidbounce.features.module.modules.legit.LegitSpeed
import net.ccbluex.liquidbounce.features.module.modules.misc.*
import net.ccbluex.liquidbounce.features.module.modules.movement.*
import net.ccbluex.liquidbounce.features.module.modules.player.*
import net.ccbluex.liquidbounce.features.module.modules.render.*
import net.ccbluex.liquidbounce.features.module.modules.world.*
import net.ccbluex.liquidbounce.features.module.modules.js.*
import net.ccbluex.liquidbounce.features.module.modules.misc.AutoPit
import net.ccbluex.liquidbounce.features.module.modules.world.Timer
import net.ccbluex.liquidbounce.utils.ClientUtils
import java.util.*


class ModuleManager : Listenable {

    val modules = TreeSet<Module> { module1, module2 -> module1.name.compareTo(module2.name) }
    private val moduleClassMap = hashMapOf<Class<*>, Module>()

    init {
        LiquidBounce.eventManager.registerListener(this)
    }

    fun getKeyBind(key: Int) = modules.filter { it.keyBind == key }
    /**
     * Register all modules
     */
    fun registerModules() {
        ClientUtils.getLogger().info("[ModuleManager] Loading modules...")

        registerModules(
                BanTips::class.java,
                Gapple::class.java,
                AutoSave::class.java,
                AutoPit::class.java,
                ScaffoldHelp::class.java,
                Health::class.java,
                PacketDebugger::class.java,
                HealthWarn::class.java,
                //Velocity3::class.java,
                HPChecker::class.java,
                AntiDebuff::class.java,
                AutoAddFriend::class.java,
                //NoSlowDown::class.java,
                AntiFireBall::class.java,
                //NewScaffold::class.java,
                Scaffold2::class.java,
                GodScaffold::class.java,
                EndDisabler::class.java,
                AutoArmor::class.java,
                AutoBow::class.java,
                AutoLeave::class.java,
                AutoPot::class.java,
                AutoSoup::class.java,
                AutoWeapon::class.java,
                BowAimbot::class.java,
                Criticals::class.java,
                KillAura::class.java,
                Trigger::class.java,
                Fly::class.java,
                ClickGUI::class.java,
                HighJump::class.java,
                InventoryMove::class.java,
                LiquidWalk::class.java,
                SafeWalk::class.java,
                WallClimb::class.java,
                Strafe::class.java,
                StrafeFix::class.java,
                Sprint::class.java,
                Teams::class.java,
                NoRotateSet::class.java,
                AntiBot::class.java,
                ChestStealer::class.java,
                Scaffold::class.java,
                CivBreak::class.java,
                Tower::class.java,
                FastBreak::class.java,
                FastPlace::class.java,
                //ItemShow::class.java,
                ESP::class.java,
                GrimVelocity::class.java,
                //NoHurt::class.java,
                //HytSpeed::class.java,
                NoSlow::class.java,
                Velocity::class.java,
                Speed::class.java,
                Tracers::class.java,
                NameTags::class.java,
                FastUse::class.java,
                Teleport::class.java,
                Fullbright::class.java,
                ItemESP::class.java,
                StorageESP::class.java,
                Projectiles::class.java,
                NoClip::class.java,
                AntiVoid::class.java,
                Nuker::class.java,
                PingSpoof::class.java,
                FastClimb::class.java,
                Step::class.java,
                AutoRespawn::class.java,
                AutoTool::class.java,
                NoWeb::class.java,
                Spammer::class.java,
                IceSpeed::class.java,
                Zoot::class.java,
                Regen::class.java,
                NoFall::class.java,
                Blink::class.java,
                NameProtect::class.java,
                NoHurtCam::class.java,
                Ghost::class.java,
                MidClick::class.java,
                XRay::class.java,
                Timer::class.java,
                Sneak::class.java,
                SkinDerp::class.java,
                Paralyze::class.java,
                GhostHand::class.java,
                AutoWalk::class.java,
                AutoBreak::class.java,
                FreeCam::class.java,
                Aimbot::class.java,
                Eagle::class.java,
                HitBox::class.java,
                AntiCactus::class.java,
                Plugins::class.java,
                AntiHunger::class.java,
                HytDisabler::class.java,
                BestAntiVoid::class.java,
                ConsoleSpammer::class.java,
                //HytHighJump::class.java,
                LongJump::class.java,
                Parkour::class.java,
                LadderJump::class.java,
                FastBow::class.java,
                MultiActions::class.java,
                AirJump::class.java,
                AutoClicker::class.java,
                NoBob::class.java,
                BlockOverlay::class.java,
                NoFriends::class.java,
                BlockESP::class.java,
                Chams::class.java,
                Clip::class.java,
                Phase::class.java,
                ServerCrasher::class.java,
                NoFOV::class.java,
                FastStairs::class.java,
                SwingAnimation::class.java,
                Derp::class.java,
                ReverseStep::class.java,
                TNTBlock::class.java,
                InvManager::class.java,
                TrueSight::class.java,
                LiquidChat::class.java,
                AntiBlind::class.java,
                NoSwing::class.java,
                BedGodMode::class.java,
                BugUp::class.java,
                Breadcrumbs::class.java,
                AbortBreaking::class.java,
                PotionSaver::class.java,
                CameraClip::class.java,
                WaterSpeed::class.java,
                Ignite::class.java,
                SlimeJump::class.java,
                MoreCarry::class.java,
                NoPitchLimit::class.java,
                Kick::class.java,
                Liquids::class.java,
                AtAllProvider::class.java,
                AirLadder::class.java,
                GodMode::class.java,
                TeleportHit::class.java,
                ForceUnicodeChat::class.java,
                ItemTeleport::class.java,
                BufferSpeed::class.java,
                SuperKnockback::class.java,
                ProphuntESP::class.java,
                AutoFish::class.java,
                Damage::class.java,
                Freeze::class.java,
                KeepContainer::class.java,
                VehicleOneHit::class.java,
                Reach::class.java,
                Rotations::class.java,
                NoJumpDelay::class.java,
                BlockWalk::class.java,
                AntiAFK::class.java,
                PerfectHorseJump::class.java,
                HUD::class.java,
                TNTESP::class.java,
                ComponentOnHover::class.java,
                KeepAlive::class.java,
                ResourcePackSpoof::class.java,
                NoSlowBreak::class.java,
                PortalMenu::class.java,
                //HighJump::class.java,
                EnchantEffect::class.java,
                //HytDash::class.java,
                //HytRun::class.java,
                SpeedMine::class.java,
                //HytFly::class.java,
                OldHitting::class.java,
                AutoHead::class.java,
                CustomTitle::class.java,
                HytGetName::class.java,
                NoFucker::class.java,
                Disabler::class.java,
                OtherDisabler::class.java,
                KeyBindManager::class.java,
                Cape::class.java,
            // NewClickGUI::class.java,
            //Velocity2::class.java,
            HytVelocity::class.java,
            MotionBlur::class.java,
            NewGUI::class.java,
            BetterHotBar::class.java,
            Test::class.java,
            NoLagBack::class.java,
            PotionRender::class.java,
            AutoL::class.java,
            FuckerHelper::class.java,
            MusicPlayer::class.java,
            ResetVL::class.java,
            AntiStuck::class.java,
            KickWarn::class.java,
            FollowTargetHud::class.java,
            AntiAim::class.java,
            LegitSpeed::class.java,
            Title::class.java,
            DMGParticle::class.java,
            AutoPlay::class.java,
            TKRun::class.java,
            CapeManager::class.java,
            FdpScaffold::class.java,
            TianKengHelp::class.java,
            HudDesigner::class.java,
            MemoryFix::class.java,
            AntiBow::class.java,
            GodLightSync::class.java,
            TpAura::class.java,
            Blur::class.java,
            FlagTips::class.java,
            kafix::class.java,
            JumpCircle::class.java,
            HYTHelper::class.java,
            PostDisabler::class.java
            //KillAura2::class.java
            )

        registerModule(NoScoreboard)
        registerModule(Fucker)
        registerModule(ChestAura)
        registerModule(Animations)

        ClientUtils.getLogger().info("[ModuleManager] Loaded ${modules.size} modules.")
    }

    /**
     * Register [module]
     */
    fun registerModule(module: Module) {
        if (!module.isSupported)
            return

        modules += module
        moduleClassMap[module.javaClass] = module

        generateCommand(module)
        LiquidBounce.eventManager.registerListener(module)
    }
    fun getModuleInCategory(Category: ModuleCategory): ArrayList<Module> {
        val moduleInCategory = ArrayList<Module>()
        for (i in this.modules) {
            if (i.category != Category)
                continue
            moduleInCategory.add(i)
        }
        return moduleInCategory
    }

    /**
     * Register [moduleClass]
     */
    private fun registerModule(moduleClass: Class<out Module>) {
        try {
            registerModule(moduleClass.newInstance())
        } catch (e: Throwable) {
            ClientUtils.getLogger().error("Failed to load module: ${moduleClass.name} (${e.javaClass.name}: ${e.message})")
        }
    }

    /**
     * Register a list of modules
     */
    @SafeVarargs
    fun registerModules(vararg modules: Class<out Module>) {
        modules.forEach(this::registerModule)
    }

    private fun registerModule(cbModule: Any?) {
        registerModule((cbModule as Class<out Module>).newInstance())
    }

    /**
     * Unregister module
     */
    fun unregisterModule(module: Module) {
        modules.remove(module)
        moduleClassMap.remove(module::class.java)
        LiquidBounce.eventManager.unregisterListener(module)
    }

    /**
     * Generate command for [module]
     */
    internal fun generateCommand(module: Module) {
        val values = module.values

        if (values.isEmpty())
            return

        LiquidBounce.commandManager.registerCommand(ModuleCommand(module, values))
    }

    /**
     * Legacy stuff
     *
     * TODO: Remove later when everything is translated to Kotlin
     */

    /**
     * Get module by [moduleClass]
     */
    fun getModule(moduleClass: Class<*>) = moduleClassMap[moduleClass]!!

    operator fun get(clazz: Class<*>) = getModule(clazz)

    /**
     * Get module by [moduleName]
     */
    fun getModule(moduleName: String?) = modules.find { it.name.equals(moduleName, ignoreCase = true) }

    /**
     * Module related events
     */

    /**
     * Handle incoming key presses
     */
    @EventTarget
    private fun onKey(event: KeyEvent) = modules.filter { it.keyBind == event.key }.forEach { it.toggle() }

    override fun handleEvents() = true
}

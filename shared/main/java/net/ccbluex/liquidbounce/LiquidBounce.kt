/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import net.ccbluex.liquidbounce.api.Wrapper
import net.ccbluex.liquidbounce.api.minecraft.util.IResourceLocation
import net.ccbluex.liquidbounce.cape.CapeAPI.registerCapeService
import net.ccbluex.liquidbounce.event.ClientShutdownEvent
import net.ccbluex.liquidbounce.event.EventManager
import net.ccbluex.liquidbounce.features.command.CommandManager
import net.ccbluex.liquidbounce.features.macro.MacroManager
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleManager
import net.ccbluex.liquidbounce.features.special.AntiForge
import net.ccbluex.liquidbounce.features.special.BungeeCordSpoof
import net.ccbluex.liquidbounce.features.special.DonatorCape
import net.ccbluex.liquidbounce.file.FileManager
import net.ccbluex.liquidbounce.injection.backend.Backend
import net.ccbluex.liquidbounce.script.ScriptManager
import net.ccbluex.liquidbounce.script.remapper.Remapper.loadSrg
import net.ccbluex.liquidbounce.tabs.BlocksTab
import net.ccbluex.liquidbounce.tabs.ExploitsTab
import net.ccbluex.liquidbounce.tabs.HeadsTab
import net.ccbluex.liquidbounce.ui.cape.GuiCapeManager
import net.ccbluex.liquidbounce.ui.client.NewGuiWelcome
import net.ccbluex.liquidbounce.ui.client.altmanager.GuiAltManager
import net.ccbluex.liquidbounce.ui.client.clickgui.ClickGui
import net.ccbluex.liquidbounce.ui.client.hud.HUD
import net.ccbluex.liquidbounce.ui.client.hud.HUD.Companion.createDefault
import net.ccbluex.liquidbounce.ui.client.keybind.KeyBindManager
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.*
import net.ccbluex.liquidbounce.utils.ClassUtils.hasForge
import net.ccbluex.liquidbounce.utils.misc.HttpUtils
import net.ccbluex.liquidbounce.utils.misc.MiscUtils
import net.minecraft.client.gui.GuiScreen
import oh.yalan.NativeMethod
import org.lwjgl.opengl.Display
import java.awt.*

object LiquidBounce {


    // Client information
        // JDK114514
    const val CLIENT_NAME = "Vestige"
    const val CLIENT_VERSION = 1.0
    const val CLIENT_CREATOR = "CCBLUEX&Paimon&Recall"//某白洲梓神秘爱好者
    const val MINECRAFT_VERSION = Backend.MINECRAFT_VERSION
    const val CLIENT_CLOUD = "Azusa-Daisuki.top"//Wait a moment

    public var USERNAME = ""

    var module: Module? =null;
    var isStarting = false
    var isUsingCloudCfg = false

    lateinit var guiwelcome: GuiScreen
    // Managers
    lateinit var moduleManager: ModuleManager
    lateinit var commandManager: CommandManager
    lateinit var eventManager: EventManager
    lateinit var fileManager: FileManager
    lateinit var macroManager: MacroManager
    lateinit var scriptManager: ScriptManager
    lateinit var combatManager: CombatManager
    // HUD & ClickGUI
    lateinit var hud: HUD

    lateinit var keyBindManager: KeyBindManager

    lateinit var clickGui: ClickGui


    // Update information
    var latestVersion = 0

    // Menu Background
    var background: IResourceLocation? = null

    lateinit var wrapper: Wrapper
    /*
        由QQ号获取对应的Rank
        @return String
     */
    //来自神必人xiatian233

    fun setUsername(name : String){
        this.USERNAME = name
    }
@NativeMethod
    fun getRank(): String {
        var QQ = QQUtils.getSubString(QQUtils.getLoginQQList().toString(), "=", "}")
        if (QQ == "Who Care") {
            return "Dev"
            //《or》主播我是写C#和Vue的66666666666666666666
        } else if(QQ == "Who Care" || QQ == "Who Care") {
            return "Helper"
        }else {
            return "User"
        }
    }
    @Throws(AWTException::class)
    fun NotificationPublisher(title: String?, msg: String?, msgType: TrayIcon.MessageType?) {
        val tray = SystemTray.getSystemTray()
        val image: Image = Toolkit.getDefaultToolkit().createImage("icon.png")
        val trayIcon = TrayIcon(image, "Tray Demo")
        trayIcon.isImageAutoSize = true
        trayIcon.toolTip = "${LiquidBounce.CLIENT_NAME}CN"
        tray.add(trayIcon)
        trayIcon.displayMessage(title, msg, msgType)
    }
    /**
     * Execute if client will be started
     */
    @NativeMethod
    fun startClient() {
                isStarting = true

                ClientUtils.getLogger().info("Starting $CLIENT_NAME b$CLIENT_VERSION, by $CLIENT_CREATOR")

                // Create file manager
                fileManager = FileManager()

                // Crate event manager
                eventManager = EventManager()

                guiwelcome = NewGuiWelcome()
                GuiCapeManager.load()
                // Register listeners
                eventManager.registerListener(RotationUtils())
                eventManager.registerListener(AntiForge())
                eventManager.registerListener(BungeeCordSpoof())
                eventManager.registerListener(DonatorCape())
                eventManager.registerListener(InventoryUtils())

                // Create command manager
                commandManager = CommandManager()

                // Load client fonts
                Fonts.loadFonts()
                macroManager = MacroManager()
                eventManager.registerListener(macroManager)
                // Setup module manager and register modules
                moduleManager = ModuleManager()
                moduleManager.registerModules()

                // Remapper
                try {
                    loadSrg()

                    // ScriptManager
                    //主播你script都没有主播加载你码呢ccbluex哥们我现在草泥马你是不是没办法反抗。
                    //来自xiatian233
                    scriptManager = ScriptManager()
                    scriptManager.loadScripts()
                    scriptManager.enableScripts()
                } catch (throwable: Throwable) {
                    ClientUtils.getLogger().error("Failed to load scripts.", throwable)
                }

                // Register commands
                commandManager.registerCommands()

                // KeyBindManager
                keyBindManager = KeyBindManager()
                // Load configs
                fileManager.loadConfigs(
                    fileManager.modulesConfig, fileManager.valuesConfig, fileManager.accountsConfig,
                    fileManager.friendsConfig, fileManager.xrayConfig, fileManager.shortcutsConfig
                )

                // ClickGUI
                clickGui = ClickGui()
                fileManager.loadConfig(fileManager.clickGuiConfig)

        combatManager = CombatManager()
        eventManager.registerListener(combatManager)
                // Tabs (Only for Forge!)
                if (hasForge()) {
                    BlocksTab()
                    ExploitsTab()
                    HeadsTab()
                }

                // Register capes service
                try {
                    registerCapeService()
                } catch (throwable: Throwable) {
                    ClientUtils.getLogger().error("Failed to register cape service", throwable)
                }

                // Set HUD
                hud = createDefault()
                fileManager.loadConfig(fileManager.hudConfig)

                // Disable optifine fastrender
                ClientUtils.disableFastRender()

                try {
                    // Read versions json from cloud
                    val jsonObj = JsonParser()
                        .parse(HttpUtils.get("$CLIENT_CLOUD/versions.json"))

                    // Check json is valid object and has current minecraft version
                    if (jsonObj is JsonObject && jsonObj.has(MINECRAFT_VERSION)) {
                        // Get official latest client version
                        latestVersion = jsonObj[MINECRAFT_VERSION].asInt
                    }
                } catch (exception: Throwable) { // Print throwable to console
                    ClientUtils.getLogger().error("Failed to check for updates.", exception)
                }

                // Load generators
                GuiAltManager.loadGenerators()

                // Set is starting status
                isStarting = false
        Display.setTitle(CLIENT_NAME + " "+CLIENT_VERSION +" | Rank: "+ getRank())
        ClientUtils.disableFastRender()
        MiscUtils().playSound(MiscUtils.SoundType.MUSIC,-8F)
        NotificationPublisher("Vestige","Client Launched.",TrayIcon.MessageType.INFO)
    }

    /**
     *
     * Execute if client will be stopped
     */
    fun stopClient() {
        // Call client shutdown
        eventManager.callEvent(ClientShutdownEvent())
        GuiCapeManager.save()
        // Save all available configs
        fileManager.saveAllConfigs()
    }

}
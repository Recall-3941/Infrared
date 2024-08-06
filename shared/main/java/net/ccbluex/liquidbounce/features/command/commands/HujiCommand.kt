package net.ccbluex.liquidbounce.features.command.commands

import net.ccbluex.liquidbounce.features.command.Command
import net.ccbluex.liquidbounce.utils.HuJiManager


/**
 *
 * Skid by Paimon.
 * @Date 2022/8/22/022
 */
class HujiCommand : Command("huji") {
    override fun execute(args: Array<String>) {
        chat("姓名: "+ HuJiManager.getName("man")+", 住址: "+HuJiManager.getLocal() + ", 互动热线: "+HuJiManager.getTel())
    }
}
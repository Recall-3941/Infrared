package net.ccbluex.liquidbounce.features.command.commands

import net.ccbluex.liquidbounce.features.command.Command
import net.ccbluex.liquidbounce.utils.HttpClientUtil


/**
 *
 * Skid by Paimon.
 * @Date 2022/8/22/022
 */
class ChaBangCommand : Command("qq") {
    /**
     * Execute commands with provided [args].
     */
    override fun execute(args: Array<String>) {
        if (args.size < 1) {
            chatSyntax(".qq QQ")
            return
        }

        if (args.size > 1){
            HttpClientUtil.main(args[1])

        }

    }
}
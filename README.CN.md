# Infrared
**_注意_**：_此源代码为远古时代编写，因此不保证可以绕过如今的服务器，当然如果您成功绕过了，也欢迎分享您的配置给我们_

一个基于Minecraft Mixin 的 黑客客户端，在Minecraft 1.12.2 Forge 版本的Minecraft 上使用。

**P.S:请不要使用PCL**

Infrared基于LiquidSense来更新，并且大概会维持每周一次的更新频率。

_作者想说的话：我们相比于其他的公益或付费客户端来说可能并没有什么突出的地方，但优势在于我们会及时听取意见并在下次更新中尽量做出调整。并且我们会尊重每一位开发者。当然也欢迎各位开发者进行批评，两位Dev也是第一次做这件事。所以欢迎一切恶意或善意的建议和评价。_
## 许可证

本项目不受任何协议约束。但我们尊重每一位开发者，因此如果您发现本源代码中有您编写的代码且您并不想开源，请您立刻联系我们，我们会以最快的速度删除。如果您发现有您编写的代码，您可以联系我们，我们会第一时间标注。

一切解释均在本README文档中。这只适用于直接位于这个干净存储库中的源代码。在开发和编译过程中，可能会使用我们没有获得任何权利的额外源代码。

此类代码我们会征求原作者的意见是否可以开源。如果找不到原作者，会征求提供者的意见或进行综合考量。



这绝不是法律建议，也没有法律约束力。只是取决于开发者们的道德水准。



**对于目前本库中没有标注原作者的代码**，您可以

-使用

-共享

-修改

-商用

-做一切你想做的，甚至倒卖

**但如果标注了源代码来源/作者，您必须立刻征求原作者的意见，否则造成的后果我们概不负责。**

这个项目全部免费。但是，请考虑以下几点：



-**您不被强迫披露您修改后的作品的源代码以及您从该项目中获取的源代码。但为了更好的开发与互助，建议您可以披露或分享您的代码或绕过思路**

-**您修改后的应用程序无需得到本开发团队认可**



执行以上操作，但不必像我们一样。





/*

_以下部分文本基本来源于LiquidSense，可能有部分修改_

*/
## 设置工作区

Infrared使用Gradle构造，因此请确保其安装正确。说明可在【Gradle的网站】上找到(https://gradle.org/install/).

1.使用`git Clone克隆存储库 https://github.com/CCBlueX/LiquidBounce`.

2.CD 放入本地存储库文件夹。

3.根据您使用的IDE，执行以下任一命令：

-对于IntelliJ:`gradlew--debug setupDevWorkspace idea genIntellijRuns build`

-对于Eclipse：`gradlew--debug setupDevWorkspace Eclipse build`

4.在IDE中将文件夹作为Gradle项目打开。

5.选择Forge或Vanilla运行配置。



## 其他库

### Mixin

Mixin可以用于在加载类之前在运行时修改类。Infrared正在使用它将代码注入Minecraft客户端。这样，我们就不必运送Mojang受版权保护的代码。如果您想了解更多信息，请查看其[文档](https://docs.spongepowered.org/5.1.0/en/plugin/internals/mixins.html).



## 贡献



我们感谢您的贡献。因此，如果您想支持我们，请随时更改Infrared的源代码并提交拉取请求。目前，我们的主要目标如下：

1.提高Infrared的视觉表现水平。

2.提升Infrared的绕过水平



如果您在其中一个或多个领域有经验并愿意为此做出贡献，或者您有能力且有意愿测试新版本，我们将非常感谢您的支持。

您可以通过以下方式联系开发者：

Alisa：2315932898

Recall：直接通过b站私信即可，qq号暂不提供
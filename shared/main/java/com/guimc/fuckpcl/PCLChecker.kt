package com.guimc.fuckpcl

import com.guimc.fuckpcl.utils.WindowUtils
import java.io.File

/**
 * A library checks Plain Craft Launcher
 * @author guimc, liuli (The UnlegitMC Team)
 */
object PCLChecker {
    /**
     * run full PCL check
     * @param mcDir minecraft folder path
     * @param deleteFolder delete PCL data folder for next PCL deleted check
     * @return check result
     */
    @JvmOverloads
    fun fullCheck(mcDir: File, deleteFolder: Boolean = true): Boolean {
        // check if there is a window named PCL
        if (titleCheck())
            return true

        // maybe the window not exists like close the window after launched , so we need to check the PCL data folder
        if (folderCheck(mcDir, deleteFolder))
            return true

        // PCL is not exists in the PC
        return false
    }

    /**
     * run PCL title check
     * check if there exists a title name contained "Plain Craft Launcher"
     * @return check result
     */
    fun titleCheck(): Boolean {
        return if (!WindowUtils.isWindows()) {
            false // PCL and the native file only support windows
        } else { // PCL Title "Plain Craft Launcher 2"
            val targetStr="Plain Craft Launcher"
            WindowUtils.getWindowNames().find { it.length < targetStr.length*2 && it.contains(targetStr) } != null
        }
    }

    /**
     * run PCL data folder check
     * @param mcDir minecraft folder path
     * @param deleteFolder delete PCL data folder for next PCL deleted check
     * @return check result
     */
    fun folderCheck(mcDir: File, deleteFolder: Boolean): Boolean {
        require(mcDir.exists()) { "Argument \"mcDir\" is not exists" }
        require(mcDir.isDirectory) { "Argument \"mcDir\" should be a folder" }

        var exists = false
        val pclDataDir = File(mcDir, "PCL")
        if (pclDataDir.exists()) {
            if (deleteFolder)
                pclDataDir.deleteRecursively()
            exists=true
        } // me need to delete all folders

        val mcVersionDir = File(mcDir, "versions")
        if (mcVersionDir.exists()) { // I think this should be existed but ...
            mcVersionDir.listFiles().forEach {
                val pclVersionDataDir = File(it, "PCL")
                if (pclVersionDataDir.exists()) {
                    if (deleteFolder)
                        pclVersionDataDir.deleteRecursively()
                    exists = true
                }
            }
        }

        return exists
    }
}
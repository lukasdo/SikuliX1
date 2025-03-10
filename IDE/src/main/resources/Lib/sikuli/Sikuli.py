#  Copyright (c) 2010-2020, sikuli.org, sikulix.com - MIT license

from __future__ import with_statement

from org.sikuli.basics import Debug as JDebug


class Debug(JDebug):
    pass


Debug.log(3, "Jython: starting init")
import time
# import __builtin__
# import __main__
import sys
import os
import inspect

# Debug.log(3, "Jython: sikuli: Sikuli: backports from Version 2: Do")
import org.sikuli.script.SX as Do

# Debug.log(3, "Jython: sikuli: Sikuli: RunTime, Setting, Debug")
import org.sikuli.support.RunTime as JRunTime


class RunTime(JRunTime):
    pass


RUNTIME = JRunTime

import org.sikuli.basics.Settings as Settings

# Debug.log(3, "Jython: sikuli: Sikuli: constants")
import org.sikuli.script.FindFailed as FindFailed
from org.sikuli.script.FindFailedResponse import *
from org.sikuli.script.Constants import *
import org.sikuli.script.Button as Button
from org.sikuli.script.Button import WHEEL_UP, WHEEL_DOWN
from org.sikuli.basics import OS

# Debug.log(3, "Jython: sikuli: Sikuli: import Region")
from Region import *

# Debug.log(3, "Jython: sikuli: Sikuli: import Screen")
# from Screen import *
import org.sikuli.script.Screen as JScreen


class Screen(JScreen):
    pass


def capture(*args):
    scrImg = SCREEN.cmdCapture(args)
    if scrImg:
        return scrImg.getStoredAt()
    return None


# Python wait() needs to be here because Java Object has a final method: wait(long timeout).
# If we want to let Sikuli users use wait(int/long timeout), we need this Python method.
def wait(target, timeout=None):
    if isinstance(target, int) or isinstance(target, long):
        target = float(target)
    if timeout == None:
        return SCREEN.wait(target)
    else:
        return SCREEN.wait(target, timeout)


# Debug.log(3, "Jython: sikuli: Sikuli: import ScreenUnion")
# from org.sikuli.script import ScreenUnion

# Debug.log(3, "Jython: sikuli: Sikuli: import Location, Offset")
import org.sikuli.script.Location as JLocation


class Location(JLocation):
    pass


import org.sikuli.script.Offset as JOffset


class Offset(JOffset):
    pass


# Debug.log(3, "Jython: sikuli: Sikuli: import Finder")
import org.sikuli.script.Finder as JFinder


class Finder(JFinder):
    pass


# Debug.log(3, "Jython: sikuli: Sikuli: import TextRecognizer")
import org.sikuli.script.TextRecognizer as JTextOCR


class TextOCR(JTextOCR):
    pass


import org.sikuli.script.OCR as JOCR


class OCR(JOCR):
    pass


# Debug.log(3, "Jython: sikuli: Sikuli: import Match")
from org.sikuli.script import Match as JMatch


class Match(JMatch):
    pass


# Debug.log(3, "Jython: sikuli: Sikuli: import ImagePath")
from org.sikuli.script import ImagePath

# Debug.log(3, "Jython: sikuli: Sikuli: import Pattern")
from org.sikuli.script import Pattern as JPattern


class Pattern(JPattern):
    pass


# Debug.log(3, "Jython: sikuli: Sikuli: import Image")
import org.sikuli.script.Image as JImage


class Image(JImage):
    pass


# Debug.log(3, "Jython: sikuli: Sikuli: Env.addHotkey")
from Env import *

# Debug.log(3, "Jython: sikuli: Sikuli: import App")
import org.sikuli.script.App as JApp


class App(JApp):
    pass


# Debug.log(3, "Jython: sikuli: Sikuli: import KeyBoard/Mouse")
from org.sikuli.script import Key as JKey


class Key(JKey):
    pass


from org.sikuli.script import KeyModifier
from org.sikuli.script.KeyModifier import KEY_CTRL, KEY_SHIFT, KEY_META, KEY_CMD, KEY_WIN, KEY_ALT
# from org.sikuli.script import Device

from org.sikuli.script import Mouse


def at():
    return Mouse.at()


# Debug.log(3, "Jython: sikuli: Sikuli: import from compare")
from org.sikuli.script.compare import DistanceComparator
from org.sikuli.script.compare import VerticalComparator
from org.sikuli.script.compare import HorizontalComparator

# Debug.log(3, "Jython: sikuli: Sikuli: init SikuliImporter")
import SikuliImporter

# Debug.log(3, "Jython: sikuli: Sikuli: import Sikulix")
from org.sikuli.script import Sikulix

# Debug.log(3, "Jython: sikuli: Sikuli: import Runner")
import org.sikuli.support.ide.Runner as Runner

import org.sikuli.support.ide.JythonSupport as JythonHelper


def load(jar):
    """
    loads a Sikuli extension (.jar) from
      1. user's sikuli data path
      2. bundle path
      3. Java classpath
    :param jar: jar filename or absolute path
    :return: True if success, False otherwise
    """
    return JythonHelper.get().load(jar)


def prepareRobot():
    return JythonHelper.get().prepareRobot()


def show():
    import org.sikuli.support.Commons as SC
    SC.show()


##
# public for options handling (property file)
##

import org.sikuli.script.Options as SXOpts


def makeOpts():
    return SXOpts.makeOpts()


def loadOpts(filePath):
    return SXOpts.loadOpts(filePath)


def getOpts(props):
    return SXOpts.getOpts(props)


def hasOpts(props):
    return SXOpts.hasOpts(props)


def setOpts(props, adict):
    return SXOpts.setOpts(props, adict)


def delOpts(props):
    return SXOpts.delOpts(props)


def saveOpts(props, filePath=None):
    if not filePath:
        return SXOpts.saveOpts(props)
    else:
        return SXOpts.saveOpts(props, filePath)


def hasOpt(props, key):
    return SXOpts.hasOpts(props, key)


def getOpt(props, key, deflt=""):
    return SXOpts.getOpt(props, key, deflt)


def getOptNum(props, key, deflt=0):
    return SXOpts.getOptNum(props, key, deflt)


def getOptInt(props, key, deflt=0):
    return SXOpts.getOptInt(props, key, deflt)


def setOpt(props, key, value):
    return SXOpts.setOpt(props, key, value)


def setOptNum(props, key, value):
    return SXOpts.setOptNum(props, key, value)


def delOpt(props, key):
    return SXOpts.delOpt(props, key)


##
# some support for handling unicode and strings
#
## use instead of print if unicode strings present
# usage: uprint(s1, u1, u2, u3, s3, ...)
#
def uprint(*args):
    for e in args[:-1]:
        if isinstance(e, str):
            print(e),
        else:
            print(e.encode("utf8")),
    if isinstance(args[-1], str):
        print(args[-1])
    else:
        print(args[-1].encode("utf8"))


##
# to make an utf8-encoded string from a str object
#
def unicd(s):
    return ucode(s)


def ucode(s):
    return (unicode(s, "utf8"))


##
# unzips the given input (string) to the given folder (string)
# relative paths are resolved against the working folder
#
def unzip(zip, target):
    import org.sikuli.basics.FileManager as FM
    return FM.unzip(str(zip), str(target))


## ----------------------------------------------------------------------
# append the given path sys.path if not yet contained
#
def addImportPath(path):
    _addModPath(path)


##
# append the given path (optional folder) to image path list if not yet contained
#
def addImagePath(*args):
    if (len(args) == 1):
        return ImagePath.append(args[0])
    elif (len(args) == 2):
        return ImagePath.append(args[0], args[1])
    return None


def addHTTPImagePath(arg):
    return ImagePath.addHTTP(arg)


def addJarImagePath(*args):
    if (len(args) == 1):
        return ImagePath.addJar(args[0])
    elif (len(args) == 2):
        return ImagePath.addJar(args[0], args[1])
    return None


##
# remove the given path from the image path (same parameter as with addImagePath)
#
def removeImagePath(*args):
    if (len(args) == 1):
        return ImagePath.remove(args[0])
    elif (len(args) == 2):
        return ImagePath.remove(args[0], args[1])
    return None


def removeHTTPImagePath(arg):
    return ImagePath.removeHTTP(arg)


def removeJarImagePath(*args):
    if (len(args) == 1):
        return ImagePath.removeJar(args[0])
    elif (len(args) == 2):
        return ImagePath.removeJar(args[0], args[1])
    return None


##
# return the current image path list
#
def getImagePath():
    return [e for e in ImagePath.get()]


##
# reset the image path, so it only contains the bundlepath
#
def resetImagePath(path=None):
    ImagePath.reset(path)


def checkImage(image):
    return ImagePath.check(image)


## ----------------------------------------------------------------------
# Sets the path for searching images in all Sikuli Script methods. <br/>
# Sikuli IDE sets this to the path of the bundle of source code (.sikuli)
# automatically. <br/>If you write Sikuli scripts using Sikuli IDE, you should
# know what you are doing.
# with no path given: Settings.BundlePath is taken
# returns true if path is valid and exists, false otherwise (no changes)
#
def setBundlePath(path=None):
    return ImagePath.setBundlePath(path)


##
# return the current bundlepath (usually the folder .sikuli)
# or None if no bundlepath is defined
# no trailing path sep
#
def getBundlePath():
    return ImagePath.getBundlePath()


##
# return the current bundlepath (usually the folder .sikuli)
# or None if no bundlepath is defined
# with a trailing path separator (for string concatenation)
#
def getBundleFolder():
    path = ImagePath.getBundlePath()
    if not path: return None
    return path + Settings.getFilePathSeperator();


##
# return the parent folder of the current bundlepath
# (usually the folder containing the current script folder.sikuli)
# or None if no bundlepath is defined
# no trailing path sep
#
def getParentPath():
    path = ImagePath.getBundlePath()
    if not path: return None
    return os.path.dirname(makePath(getBundlePath()));


##
# return the parent folder of the current bundlepath
# (usually the folder containing the current script folder.sikuli)
# or None if no bundlepath is defined
# no trailing path sep
#
def getParentFolder():
    path = getParentPath()
    if not path: return None
    return path + Settings.getFilePathSeperator();


##
# make a valid path by by using os.path.join() with the given elements
# always without a trailing path separator
#
def makePath(*paths):
    if len(paths) == 0: return None
    path = paths[0]
    if len(paths) > 1:
        for p in paths[1:]:
            path = os.path.join(path, p)
    if path[-1] == Settings.getFilePathSeperator():
        return os.path.dirname(path)
    return path


##
# make a valid path by by using os.path.join() with the given elements
# with a trailing path separator (for string concatenation)
#
def makeFolder(*paths):
    path = makePath(*paths)
    if not path: return None
    path = path + Settings.getFilePathSeperator()
    return path


## ----------------------------------------------------------------------
# Sikuli shows actions (click, dragDrop, ... etc.) if this flag is set to <i>True</i>.
# The default setting is <i>False</i>.
#
def setShowActions(flag):
    Settings.setShowActions(flag)


## ----------------------------------------------------------------------
# set location, where the center of the pop... should be
# no-args: use center screen where SikuliX is running (default)
# is used until changed again
def popat(*args):
    if len(args) == 0:
        return Sikulix.popat()
    elif len(args) > 1:
        return Sikulix.popat(args[0], args[1])
    else:
        return Sikulix.popat(args[0])


# Shows a message dialog containing the given message.
# @param msg The given message string.
# @param title gets the window title.
def popup(msg, title="Sikuli Info"):
    # Sikulix.popup(msg, title)
    Do.popup(msg, title)


# Show error popup (special icon) containing the given message.
# @param msg The given message string.
# @param title gets the window title.
def popError(msg, title="Sikuli Error"):
    # Sikulix.popError(msg, title)
    Do.popError(msg, title)


# Show a popup containing the given message asking for yes or no
# @param msg The given message string.
# @param title gets the window title.
# @return True if answered Yes, else False
def popAsk(msg, title="Sikuli Decision"):
    # return Sikulix.popAsk(msg, title)
    return Do.popAsk(msg, title)


##
# Shows a question-message dialog requesting input from the user.
# @param msg The message to display.
# @param default The preset text of the input field (default empty).
# @param title the title for the dialog (default: Sikuli input request)
# @param hidden =true makes the dialog run as a password input (input hidden with bullets)
# @return The user's input string.
#
def input(msg="", default="", title="Sikuli Input", hidden=False):
    if (hidden):
        default = ""
    # return Sikulix.input(msg, default, title, hidden)
    return Do.input(msg, title, default, hidden)


##
# Shows a dialog request to enter text in a multiline text field
# Though not all text might be visible, everything entered is delivered with the returned text
# The main purpose for this feature is to allow pasting text from somewhere
# @param msg the message to display.
# @param title the title for the dialog (default: Sikuli input request)
# @param lines the maximum number of lines visible in the text field (default 9)
# @param width the maximum number of characters visible in one line (default 20)
# @return The user's input including the line breaks.
def inputText(msg="", title="", lines=0, width=0, text=""):
    return Sikulix.inputText(msg, title, lines, width, text)


##
# Shows a dialog requesting to select an entry from the drop down list
# @param msg the message to display.
# @param title the title for the dialog
def select(msg="", title="Sikuli Selection", options=(), default=None):
    optionsLen = len(options)
    if optionsLen == 0:
        return ""
    try:
        ndefault = 0 + default;
        if ndefault > -1 and ndefault < optionsLen:
            default = options[ndefault]
    except:
        pass
    # return Sikulix.popSelect(msg, title, options, default)
    optionString = "";
    for option in options:
        optionString += str(len(option) + 1000) + option
    return Do.popSelect(msg, title, default, optionString)


def popFile(title="Select File or Folder"):
    # return Sikulix.popFile(title)
    return Do.popFile("", title)


## ----------------------------------------------------------------------
# set the default screen to given or primary screen
#
# TODO where else to remember an opened remote screen?
remoteScreen = None
import org.sikuli.support.devices.ScreenDevice as SD
SCREEN = None

def use(scr=None, remote=False, fromWith=False):
    global SCREEN
    global remoteScreen
    if remoteScreen:
        remoteScreen.close()
        remoteScreen = None
    if not scr:
        newScreen = SD.getPrimaryScreen()
    else:
        newScreen = scr
    if remote or fromWith:
        theGlobals = inspect.currentframe().f_back.f_back.f_globals
    else:
        theGlobals = inspect.currentframe().f_back.f_globals
    _setSCREEN(newScreen, theGlobals)
    if remote:
        remoteScreen = SCREEN

##
# set the default screen to given remote screen
#
def useRemote(adr, port=0):
    global remoteScreen
    import org.sikuli.script.ScreenRemote as SR
    SCREEN = SR(adr, str(port))
    if SCREEN.isValid():
        return use(SCREEN, True)
    else:
        return None

## ----------------------------------------------------------------------
# Switches the frontmost application to the given application.
# If the given application is not running, it will be launched by openApp()
# automatically. <br/>
# Note: On Windows, Sikuli searches in the text on the title bar
# instead of the application name.
# @param app The name of the application. (case-insensitive)
#
def switchApp(app):
    return App.open(app)


##
# Opens the given application. <br/>
# @param app The name of an application if it is in the environment variable PATH, or the full path to an application.
#
def openApp(app):
    return App.open(app)


##
# Closes the given application. <br/>
# @param app The name of the application. (case-insensitive)
#
def closeApp(app):
    return App.close(app)


##
# Sleeps until the given amount of time in seconds has elapsed.
# @param sec The amount of sleeping time in seconds.
def sleep(sec):
    time.sleep(sec)


##
# shutdown and return given exit code
#
def exit(code=0):
    global remoteScreen
    if remoteScreen:
        remoteScreen.close()
        remoteScreen = None
    # Sikulix.cleanUp(code)
    sys.exit(code)


## ----------------------------------------------------------------------
# Runs the given string command.
# @param cmd The given string command.
# @return Returns the output from the executed command.
def run(cmd):
    return Sikulix.run(cmd)


# Runs the script given by absolute or relative path (./ same folder as calling script)
# @param script The given script path.
# @args the parameters for the called script (sys.argv)
# @return returns the scripts return code given with exit(n)
def runScript(script, *args):
    return Runner.runScript(unicd(script), args, None)


def getLastReturnCode():
    return RUNTIME.getLastScriptRunReturnCode()


##
# helper functions, that can be used when sorting lists of regions
#
def byDistanceTo(x, y=None):
    """ Method to compare two Region objects by distance of their top left.
      or a regions top left to the given point by coordinates"""
    return DistanceComparator(x, y)


def byX(m):
    """ Method to compare two Region objects by x value. """
    return HorizontalComparator().compare


def byY(m):
    """ Method to compare two Region objects by y value. """
    return VerticalComparator().compare


def verticalComparator():
    """ Method to compare two Region objects by y value. """
    return VerticalComparator().compare


def horizontalComparator():
    """ Method to compare two Region objects by x value. """
    return HorizontalComparator().compare


def distanceComparator(x, y=None):
    """ Method to compare two Region objects by distance of their top left.
      or a regions top left to the given point by coordinates"""
    if y is None:
        return DistanceComparator(x).compare  # x is Region or Location
    return DistanceComparator(x, y).compare  # x/y as coordinates


##
################## internal use only ###########################################
#
def _addModPath(path):
    if path[-1] == Settings.getFilePathSeperator():
        path = path[:-1]
    if not path in sys.path:
        sys.path.append(path)


def _exposeAllMethods(anyObject, saved, theGlobals, exclude_list):
    if not exclude_list:
        exclude_list = ['class', 'classDictInit', 'clone', 'equals', 'finalize',
                        'getClass', 'hashCode', 'notify', 'notifyAll',
                        'toGlobalCoord', 'getLocationFromPSRML', 'getRegionFromPSRM',
                        'create', 'waitAll',
                        'updateSelf', 'findNow', 'findAllNow', 'getEventManager',
                        'lastMatch', 'lastMatches', 'lastScreenImage', 'lastScreenImageFile',
                        'capture', 'wait', 'lineList', 'wordList', 'all', 'image',
                        'reset'
                        ]
    Debug.log(4, "Sikuli: _exposeAllMethods: %s called from: %s", anyObject, theGlobals['__name__'])
    tosave = []
    if not saved:
        saved = []
    for name in dir(anyObject):
        if name in exclude_list: continue
        Debug.log(4, "Sikuli: _exposeAllMethods: checking: %s", name)
        try:
            if name == "wordList": Debug.log(4, "Sikuli: _exposeAllMethods: checking1: %s", name)
            attr = getattr(anyObject, name)
            if name == "wordList": Debug.log(4, "Sikuli: _exposeAllMethods: checking2: %s", name)
            if not inspect.ismethod(attr): continue
        except:
            continue
        if name[0] != '_' and name[:7] != 'super__':
            try:
                saved.remove(name)
            except:
                pass
            tosave.append(name)
            theGlobals[name] = eval("anyObject." + name)
            Debug.log(4, "Sikuli: _exposeAllMethods: added: %s", name)
            if name == 'checkWith': Debug.log(4, "%s %s", name, str(dict[name])[1:])
    for name in saved:
        if name in theGlobals:
            Debug.log(4, "Sikuli: _exposeAllMethods: removed: %s", name)
            theGlobals.pop(name)
    return tosave

## ----------------------------------------------------------------------
def reset():
    JScreen.resetMonitors();
    ALL = SCREEN.all().getRegion()

def resetBeforeScriptStart():
    scr = SD.getPrimaryScreen();
    scr.reset()
    theGlobals = inspect.currentframe().f_back.f_globals
    _setSCREEN(scr,theGlobals)

def _setSCREEN(scr, theGlobals):
    global SCREEN
    SCREEN = scr
    globals()['SIKULISAVED'] = _exposeAllMethods(SCREEN, globals().get('SIKULISAVED'), theGlobals, None)
    theGlobals['SCREEN'] = SCREEN

############### set SCREEN as primary screen at startup ################
ALL = JScreen.all().getRegion()
Debug.log(3, "Jython: ending init")

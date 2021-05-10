/*
 * Copyright (c) 2010-2020, sikuli.org, sikulix.com - MIT license
 */
package org.sikuli.ide;

import org.sikuli.basics.Debug;
import org.sikuli.basics.PreferencesUser;
import org.sikuli.basics.Settings;
import org.sikuli.script.Sikulix;
import org.sikuli.script.*;
import org.sikuli.script.support.Commons;
import org.sikuli.script.support.IScreen;
import org.sikuli.script.support.RunTime;
import org.sikuli.script.support.devices.HelpDevice;
import org.sikuli.script.support.gui.SikuliIDEI18N;
import org.sikuli.util.EventObserver;
import org.sikuli.util.EventSubject;
import org.sikuli.util.OverlayCapturePrompt;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.Element;
import javax.swing.text.*;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

//import java.awt.Image;

class ButtonCapture extends ButtonOnToolbar implements ActionListener, Cloneable, EventObserver {

  private static final String me = "ButtonCapture: ";
  protected EditorPane _codePane;
  private boolean captureCancelled = false;
  private EditorPatternLabel _lbl = null;
  private String givenName = "";

  public static boolean debugTrace = true;

  public ButtonCapture() {
    super();
    URL imageURL = SikulixIDE.class.getResource("/icons/sxcapture-x.png");
    setIcon(new ImageIcon(imageURL));
    PreferencesUser pref = PreferencesUser.get();
    String strHotkey = Key.convertKeyToText(
        pref.getCaptureHotkey(), pref.getCaptureHotkeyModifiers());
    setToolTipText(SikulixIDE._I("btnCaptureHint", strHotkey));
    setText(SikulixIDE._I("btnCaptureLabel"));
    addActionListener(this);
  }

//  public ButtonCapture(EditorPane codePane, Element elmLine) {
//    this();
//    _line = elmLine;
//    _codePane = codePane;
//    setUI(UIManager.getUI(this));
//    setBorderPainted(true);
//    setCursor(new Cursor(Cursor.HAND_CURSOR));
//    setText(null);
//    URL imageURL = SikulixIDE.class.getResource("/icons/capture-small.png");
//    setIcon(new ImageIcon(imageURL));
//  }

//  public ButtonCapture(EditorPatternLabel lbl) {
//    // for internal use with the image label __CLICK-TO-CAPTURE__
//    super();
//    _line = null;
//    _codePane = null;
//    _lbl = lbl;
//  }

  @Override
  public void actionPerformed(ActionEvent e) {
    Debug.log(3, "ButtonCapture: capture started");
    captureWithAutoDelay();
  }

  public void captureWithAutoDelay() {
    PreferencesUser pref = PreferencesUser.get();
    int delay = (int) (pref.getCaptureDelay() * 1000.0) + 1;
    capture(delay);
  }

  ScreenImage sImgNonLocal = null;

  public void capture(int delay) {
    String line = "";
    if (SikulixIDE.notHidden()) {
      // Set minimum delay if IDE is visible to give
      // the IDE some time to vanish before taking the
      // screenshot. IDE might already be hidden in
      // in case of capture hot key.
      delay = Math.max(delay, 500);
      SikulixIDE.doHide();
    }

    givenName = SikulixIDE.get().getImageNameFromLine();

    RunTime.pause(delay);
    Screen.doPrompt("Select an image", this);

//TODO capture on Android
//    defaultScreen = SikulixIDE.getDefaultScreen();
//    if (defaultScreen == null) {
//      Screen.doPrompt("Select an image", this);
//    } else {
//      if (HelpDevice.isAndroid(defaultScreen) && Sikulix.popAsk("Android capture")) {
//        new Thread() {
//          @Override
//          public void run() {
//            sImgNonLocal = (ScreenImage) defaultScreen.action("userCapture");
//            ButtonCapture.this.update((EventSubject) null);
//          }
//        }.start();
//      } else {
//        ButtonCapture.this.update((EventSubject) null);
//      }
//    }
  }

  @Override
  public void update(EventSubject event) {
    Debug.log(3, "ButtonCapture: finished");
    BufferedImage capturedImage;
    BufferedImage screenShot = null;
    OverlayCapturePrompt ocp = (OverlayCapturePrompt) event;
    if (ocp == null) { //TODO Android
      capturedImage = sImgNonLocal.getImage();
    } else {
      capturedImage = ocp.getSelection().getImage();
      screenShot = ocp.getOriginal().getImage();
      Screen.closePrompt();
    }
    if (capturedImage != null) {
      if (givenName.isEmpty()) {
        final PreferencesUser prefs = PreferencesUser.get();
        if (prefs.getAutoNamingMethod() == PreferencesUser.AUTO_NAMING_OFF) {
          givenName = (String) JOptionPane.showInputDialog(
              SikulixIDE.get(),
              SikuliIDEI18N._I("msgEnterScreenshotFilename"),
              SikuliIDEI18N._I("dlgEnterScreenshotFilename"),
              JOptionPane.PLAIN_MESSAGE,
              null,
              null,
              "noname");
        }
        if (givenName == null || givenName.isEmpty()) {
          givenName = Settings.getTimestamp();
        }
//        if (prefs.getAutoNamingMethod() == PreferencesUser.AUTO_NAMING_TIMESTAMP) {
//          ;
//        } else if (naming == PreferencesUser.AUTO_NAMING_OCR) {
//          filename = PatternPaneNaming.getFilenameFromImage(capturedImage.get());
//          if (filename == null || filename.length() == 0) {
//            filename = Settings.getTimestamp();
//          }
//        } else {
//          String nameOCR = "";
//          try {
//            nameOCR = PatternPaneNaming.getFilenameFromImage(capturedImage.get());
//          } catch (Exception e) {
//          }
//          filename = ;
//        }
      }
    }
    SikulixIDE.PaneContext context = SikulixIDE.get().getActiveContext();
    final File imgFile = new File(context.getImageFolder(), givenName + ".png");
    try {
      ImageIO.write(capturedImage, "png",imgFile);
      if (context.getScreenshotFolder().exists()) {
        ImageIO.write(screenShot, "png", new File(context.getScreenshotFolder(), givenName + ".png"));
      }
    } catch (IOException e) {
    }

    if (context.getShowThumbs()) {
      SikulixIDE.get().insertImageButton(context, imgFile);
    } else {
      context.getPane().insertString("\"" + givenName + "\"");
    }

    if (ocp != null) {
      Screen.resetPrompt(ocp);
    }
    SikulixIDE.showAgain();
  }

  private void captureCompleted(BufferedImage imgCaptured, BufferedImage screenShot) {
  }

  //<editor-fold defaultstate="collapsed" desc="RaiMan not used">
  /*public boolean hasNext() {
   * return false;
   * }*/
  /*public CaptureButton getNextDiffButton() {
   * return null;
   * }*/
  /*public void setParentPane(SikuliPane parent) {
   * _codePane = parent;
   * }*/
  /*public void setDiffMode(boolean flag) {
   * }*/
  /*public void setSrcElement(Element elmLine) {
   * _line = elmLine;
   * }*/
  //</editor-fold>

  private boolean replaceButton(Element src, String imgFullPath) {
    if (captureCancelled) {
      if (_codePane.context.getShowThumbs() && PreferencesUser.get().getPrefMoreImageThumbs()
          || !_codePane.context.getShowThumbs()) {
        return true;
      }
    }
    int start = src.getStartOffset();
    int end = src.getEndOffset();
    int old_sel_start = _codePane.getSelectionStart(),
        old_sel_end = _codePane.getSelectionEnd();
    try {
      StyledDocument doc = (StyledDocument) src.getDocument();
      String text = doc.getText(start, end - start);
      Debug.log(3, text);
      for (int i = start; i < end; i++) {
        Element elm = doc.getCharacterElement(i);
        if (elm.getName().equals(StyleConstants.ComponentElementName)) {
          AttributeSet attr = elm.getAttributes();
          Component com = StyleConstants.getComponent(attr);
          boolean isButton = com instanceof ButtonCapture;
          boolean isLabel = com instanceof EditorPatternLabel;
          if (isButton || isLabel && ((EditorPatternLabel) com).isCaptureButton()) {
            Debug.log(5, "button is at " + i);
            int oldCaretPos = _codePane.getCaretPosition();
            _codePane.select(i, i + 1);
            if (!_codePane.context.getShowThumbs()) {
              _codePane.insertString((new EditorPatternLabel(_codePane, imgFullPath, true)).toString());
            } else {
              if (PreferencesUser.get().getPrefMoreImageThumbs()) {
                com = new EditorPatternButton(_codePane, imgFullPath);
              } else {
                if (captureCancelled) {
                  com = new EditorPatternLabel(_codePane, "");
                } else {
                  com = new EditorPatternLabel(_codePane, imgFullPath, true);
                }
              }
              _codePane.insertComponent(com);
            }
            _codePane.setCaretPosition(oldCaretPos);
            break;
          }
        }
      }
    } catch (BadLocationException ble) {
      Debug.error(me + "Problem inserting Button!\n%s", ble.getMessage());
    }
    _codePane.select(old_sel_start, old_sel_end);
    _codePane.requestFocus();
    return true;
  }

  protected void insertAtCursor(EditorPane pane, Image capturedImage) {
    if (!pane.context.getShowThumbs()) {
      pane.insertString("\"" + capturedImage.getName() + "\"");
    } else {
      if (PreferencesUser.get().getPrefMoreImageThumbs()) {
        EditorPatternButton comp = EditorPatternButton.createFromImage(pane, capturedImage, null);
        if (comp != null) {
          pane.insertComponent(comp);
        }
      } else {
        EditorPatternLabel label = new EditorPatternLabel(pane, capturedImage.fileName(), true);
        pane.insertComponent(label);
      }
    }
//TODO set Caret
    pane.requestFocus();
  }

  @Override
  public String toString() {
    return "\"__CLICK-TO-CAPTURE__\"";
  }
}

/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may
 *    "Alice" appear in their name, without prior written permission of
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is
 *    contributed by Electronic Arts Inc. and may be used for personal,
 *    non-commercial, and academic use only. Redistributions of any program
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in
 *    The Alice 3.0 Art Gallery License.
 *
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *******************************************************************************/
package org.alice.media.youtube.croquet;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import edu.cmu.cs.dennisc.java.awt.ComponentUtilities;
import edu.cmu.cs.dennisc.javax.swing.option.Dialogs;
import org.alice.ide.croquet.models.help.AbstractLoginComposite;
import org.alice.ide.croquet.models.help.LogInOutComposite;
import org.alice.ide.croquet.models.help.LogInOutListener;
import org.alice.ide.video.preview.VideoComposite;
import org.alice.media.youtube.core.YouTubeEvent;
import org.alice.media.youtube.core.YouTubeEvent.EventType;
import org.alice.media.youtube.core.YouTubeListener;
import org.alice.media.youtube.core.YouTubeUploader;
import org.alice.media.youtube.croquet.views.UploadToYouTubeStatusPane;
import org.alice.media.youtube.croquet.views.UploadView;
import org.alice.media.youtube.login.YouTubeLoginComposite;
import org.alice.stageide.StageIDE;
import org.lgna.croquet.ActionOperation;
import org.lgna.croquet.BooleanState;
import org.lgna.croquet.CancelException;
import org.lgna.croquet.Operation;
import org.lgna.croquet.StringState;
import org.lgna.croquet.WizardPageComposite;

import com.google.gdata.data.media.MediaFileSource;
import com.google.gdata.data.media.mediarss.MediaCategory;
import com.google.gdata.data.media.mediarss.MediaDescription;
import com.google.gdata.data.media.mediarss.MediaKeywords;
import com.google.gdata.data.media.mediarss.MediaTitle;
import com.google.gdata.data.youtube.VideoEntry;
import com.google.gdata.data.youtube.YouTubeMediaGroup;
import com.google.gdata.data.youtube.YouTubeNamespace;

import edu.cmu.cs.dennisc.java.awt.FileDialogUtilities;
import edu.cmu.cs.dennisc.java.io.FileUtilities;
import org.lgna.croquet.edits.Edit;
import org.lgna.croquet.history.UserActivity;

/**
 * @author Matt May
 */
public class UploadComposite extends WizardPageComposite<UploadView, ExportToYouTubeWizardDialogComposite> {
  private final YouTubeUploader uploader = new YouTubeUploader();

  private final LogInOutComposite logInOutComposite = new LogInOutComposite(UUID.fromString("294cb10a-ad2f-42cf-8159-ac859c7fe792"), new YouTubeLoginComposite(this));

  private final StringState titleState = this.createStringState("titleState", "Alice Video");
  private final BooleanState isPrivateState = this.createBooleanState("isPrivateState", false);
  //  private final ListSelectionState<String> videoCategoryState;
  private final StringState descriptionState = this.createStringState("descriptionState", "");
  private final StringState tagsState = this.createStringState("tagsState", "Alice3");

  private final Status errorNotLoggedIn = createErrorStatus("errorNotLoggedIn");
  private final Status errorCannotConnect = createErrorStatus("errorCannotConnect");
  private final Status noTittle = createErrorStatus("errorNoTittle");
  private final Status noDescriptions = createWarningStatus("warningNoDescriptions");
  private final Status noTags = createWarningStatus("warningNoTags");

  private final VideoComposite videoComposite = new VideoComposite();
  private boolean isUploaded = false;
  private boolean categoriesEnabled = false;

  public UploadComposite(ExportToYouTubeWizardDialogComposite owner) {
    super(UUID.fromString("5c7ee7ee-1c0e-4a92-ac4e-bca554a0d6bc"), owner);
    //    uploader.addYouTubeListener( this.getUploadOperation() );
    //    this.videoCategoryState = this.createListSelectionState( "videoCategoryState" ), String.class, org.alice.ide.croquet.codecs.StringCodec.SINGLETON, 0, YouTubeCategories.getCategories() );
    this.registerSubComposite(this.videoComposite);
    this.registerSubComposite(logInOutComposite);
    logInOutComposite.addListener(this.logInOutListener);
    //    videoCategoryState.setEnabled( categoriesEnabled );
  }

  private final ActionOperation exportToFileOperation = this.createActionOperation("exportToFileOperation", new Action() {
    @Override
    public Edit perform(UserActivity userActivity, InternalActionOperation source) throws CancelException {

      File videosDirectory = StageIDE.getActiveInstance().getVideosDirectory();

      String filename;
      String title = titleState.getValue();
      if ((title != null) && (title.length() > 0)) {
        filename = title + ".webm";
      } else {
        filename = "*.webm";
      }

      File exportFile = FileDialogUtilities.showSaveFileDialog(UploadComposite.this.getView().getAwtComponent(), "Save Video", videosDirectory, filename, FileUtilities.createFilenameFilter("webm"), "webm");
      if (exportFile != null) {
        try {
          FileUtilities.copyFile(getOwner().getTempRecordedVideoFile(), exportFile);
        } catch (IOException ioe) {
          Dialogs.showWarning("cannot export file: " + exportFile);
        }
      }
      return null;
    }
  });
  private final LogInOutListener logInOutListener = new LogInOutListener() {

    @Override
    public void fireLoggedOut(AbstractLoginComposite<?> login) {
      refreshOwnerStatus();
    }

    @Override
    public void fireLoggedIn(AbstractLoginComposite<?> login) {
      refreshOwnerStatus();
    }
  };

  public VideoComposite getVideoComposite() {
    return this.videoComposite;
  }

  public YouTubeUploader getUploader() {
    return this.uploader;
  }

  public LogInOutComposite getLoginComposite() {
    return this.logInOutComposite;
  }

  public Operation getExportToFileOperation() {
    return this.exportToFileOperation;
  }

  public StringState getTitleState() {
    return this.titleState;
  }

  public BooleanState getIsPrivateState() {
    return this.isPrivateState;
  }

  //  public ListSelectionState<String> getVideoCategoryState() {
  //    return this.videoCategoryState;
  //  }

  public StringState getDescriptionState() {
    return this.descriptionState;
  }

  public StringState getTagsState() {
    return this.tagsState;
  }

  @Override
  protected UploadView createView() {
    return new UploadView(this);
  }

  @Override
  public void handlePreActivation() {
    super.handlePreActivation();
    this.videoComposite.getView().setUri(this.getOwner().getTempRecordedVideoFile().toURI());
  }

  @Override
  public void handlePostDeactivation() {
    this.videoComposite.getView().setUri(null);
    super.handlePostDeactivation();
  }

  @Override
  public Status getPageStatus() {
    //    if( true ) {
    //      return IS_GOOD_TO_GO_STATUS;
    //    }
    //    uploadOperation.setEnabled( false );
    Status rv = IS_GOOD_TO_GO_STATUS;
    if (!logInOutComposite.getComposite().getIsLoggedIn().getValue()) {
      setEnabled(false);
      if (logInOutComposite.getCanConnect()) {
        return errorNotLoggedIn;
      } else {
        return errorCannotConnect;
      }
    } else {
      setEnabled(true);
      if (titleState.getValue().length() == 0) {
        return noTittle;
      } else if (descriptionState.getValue().length() == 0) {
        rv = noDescriptions;
      } else if (tagsState.getValue().length() == 0) {
        rv = noTags;
      }
    }
    return rv;
  }

  private void setEnabled(boolean isEnabled) {
    final boolean IS_VIEW_BASED = true; //what is this? (mmay)
    if (IS_VIEW_BASED) {
      for (Component awtComponent : this.getView().getYoutubeDetailsPanel().getAwtComponent().getComponents()) {
        awtComponent.setEnabled(isEnabled);
        if (awtComponent instanceof JScrollPane) {
          JScrollPane jScrollPane = (JScrollPane) awtComponent;
          jScrollPane.getViewport().getView().setEnabled(isEnabled);
        }
        if (awtComponent instanceof JComboBox) {
          awtComponent.setEnabled(isEnabled && categoriesEnabled);
        }
      }
    } else {
      titleState.setEnabled(isEnabled);
      descriptionState.setEnabled(isEnabled);
      tagsState.setEnabled(isEnabled);
      //      videoCategoryState.setEnabled( isEnabled && categoriesEnabled );
      isPrivateState.setEnabled(isEnabled);
    }
  }

  @Override
  public void resetData() {
    titleState.setValueTransactionlessly("");
    descriptionState.setValueTransactionlessly("");
    tagsState.setValueTransactionlessly("Alice3");
  }

  YouTubeListener listener = new YouTubeListener() {

    @Override
    public void youTubeEventTriggered(YouTubeEvent event) {
      if (event.getType() == EventType.UPLOAD_SUCCESS) {
        UploadComposite.this.isUploaded = true;
      }
    }
  };

  public boolean tryToUpload() {
    JFrame parent = ComponentUtilities.getRootJFrame(getView().getAwtComponent());
    UploadToYouTubeStatusPane statusPane = new UploadToYouTubeStatusPane(parent, uploader);
    uploader.addYouTubeListener(listener);
    uploader.addYouTubeListener(statusPane);
    statusPane.setSize(500, 400);
    VideoEntry entry = new VideoEntry();

    MediaFileSource source = new MediaFileSource(this.getOwner().getTempRecordedVideoFile(), "video/quicktime");
    entry.setMediaSource(source);
    YouTubeMediaGroup mediaGroup = entry.getOrCreateMediaGroup();

    MediaTitle title = new MediaTitle();
    title.setPlainTextContent(titleState.getValue().trim());
    mediaGroup.setTitle(title); //title

    MediaDescription mediaDescription = new MediaDescription();
    mediaDescription.setPlainTextContent(descriptionState.getValue().trim());
    mediaGroup.setDescription(mediaDescription); //description]

    MediaKeywords keywords = new MediaKeywords();
    String[] arr = tagsState.getValue().split(",");
    for (String s : arr) {
      keywords.addKeyword(s.trim());
    }
    mediaGroup.setKeywords(keywords); //tags

    String category = "Education";
    mediaGroup.addCategory(new MediaCategory(YouTubeNamespace.CATEGORY_SCHEME, category)); //category
    mediaGroup.setPrivate(isPrivateState.getValue()); //isPrivate
    try {
      uploader.uploadVideo(entry);
      statusPane.setVisible(true);
    } catch (IOException e) {
    }
    return isUploaded;
  }
}

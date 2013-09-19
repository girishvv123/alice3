/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
 */
package org.alice.imageeditor.croquet;

/**
 * @author Dennis Cosgrove
 */
public class ImageEditorFrame extends org.lgna.croquet.FrameComposite<org.alice.imageeditor.croquet.views.ImageEditorPane> {
	private static java.io.File getBestGuessPicturesDirectory() {
		java.io.File defaultDirectory = edu.cmu.cs.dennisc.java.io.FileUtilities.getDefaultDirectory();
		java.io.File userDirectory;
		if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isWindows() ) {
			userDirectory = defaultDirectory.getParentFile();
		} else {
			userDirectory = defaultDirectory;
		}
		java.io.File file = new java.io.File( userDirectory, "Pictures" );
		if( file.isDirectory() ) {
			return file;
		} else {
			return defaultDirectory;
		}
	}

	private static final String DEFAULT_ROOT_DIRECTORY_PATH = getBestGuessPicturesDirectory().getAbsolutePath();

	private final org.lgna.croquet.ValueHolder<java.awt.Image> imageHolder = new org.lgna.croquet.ValueHolder<java.awt.Image>();

	private final org.lgna.croquet.ValueHolder<String> pathHolder = new org.lgna.croquet.ValueHolder<String>();

	private final java.util.List<java.awt.Shape> shapes = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();

	private final org.lgna.croquet.Operation clearOperation = this.createActionOperation( this.createKey( "clearOperation" ), new Action() {
		public org.lgna.croquet.edits.Edit perform( org.lgna.croquet.history.CompletionStep<?> step, InternalActionOperation source ) throws org.lgna.croquet.CancelException {
			clearShapes();
			//todo
			getView().repaint();
			return null;
		}
	} );

	private final org.lgna.croquet.StringState rootDirectoryState = this.createPreferenceStringState( this.createKey( "rootDirectoryState" ), DEFAULT_ROOT_DIRECTORY_PATH, null );

	private final org.lgna.croquet.Operation browseOperation = this.createActionOperation( this.createKey( "browseOperation" ), new Action() {
		public org.lgna.croquet.edits.Edit perform( org.lgna.croquet.history.CompletionStep<?> step, org.lgna.croquet.AbstractComposite.InternalActionOperation source ) throws org.lgna.croquet.CancelException {
			String rootDirectoryPath = rootDirectoryState.getValue();
			javax.swing.JFileChooser jFileChooser = new javax.swing.JFileChooser();
			if( ( rootDirectoryPath != null ) && ( rootDirectoryPath.length() > 0 ) ) {
				java.io.File rootDirectory = new java.io.File( rootDirectoryPath );
				if( rootDirectory.exists() ) {
					jFileChooser.setCurrentDirectory( rootDirectory );
				}
			}
			jFileChooser.setDialogType( javax.swing.JFileChooser.SAVE_DIALOG );
			jFileChooser.setApproveButtonText( "Set Root Directory" );
			jFileChooser.setFileSelectionMode( javax.swing.JFileChooser.DIRECTORIES_ONLY );
			java.awt.Component awtParent = ImageEditorFrame.this.getView().getAwtComponent();
			int result = jFileChooser.showSaveDialog( awtParent );
			if( result == javax.swing.JFileChooser.APPROVE_OPTION ) {
				java.io.File file = jFileChooser.getSelectedFile();
				if( file != null ) {
					if( file.exists() ) {
						if( file.isDirectory() ) {
							rootDirectoryState.setValueTransactionlessly( file.getAbsolutePath() );
						} else {
							org.lgna.croquet.Application.getActiveInstance().showMessageDialog( "file is not a directory" );
						}
					} else {
						org.lgna.croquet.Application.getActiveInstance().showMessageDialog( "file is does not exist" );
					}
				} else {
					org.lgna.croquet.Application.getActiveInstance().showMessageDialog( "file is null" );
				}
				return null;
			} else {
				throw new org.lgna.croquet.CancelException();
			}
		}
	} );

	private class FilenameComboBoxModel implements javax.swing.ComboBoxModel {
		private boolean isWorking;
		private java.util.List<java.io.File> data;

		private Object selectedItem;

		private final java.util.List<javax.swing.event.ListDataListener> listDataListeners = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();

		public int getSize() {
			int isWorkingDelta = this.isWorking ? 1 : 0;
			return this.data != null ? this.data.size() + isWorkingDelta : 0;
		}

		public Object getElementAt( int index ) {
			return ( index < this.data.size() ) ? this.data.get( index ) : null;
		}

		public Object getSelectedItem() {
			return this.selectedItem;
		}

		public void setSelectedItem( Object selectedItem ) {
			if( edu.cmu.cs.dennisc.equivalence.EquivalenceUtilities.areEquivalent( this.selectedItem, selectedItem ) ) {
				//pass
			} else {
				if( ( this.selectedItem != null ) && ( selectedItem != null ) && this.selectedItem.toString().contentEquals( selectedItem.toString() ) ) {
					//pass
				} else {
					this.selectedItem = selectedItem;
					this.fireContentsChanged();
				}
			}
		}

		public void addListDataListener( javax.swing.event.ListDataListener listener ) {
			this.listDataListeners.add( listener );
		}

		public void removeListDataListener( javax.swing.event.ListDataListener listener ) {
			this.listDataListeners.remove( listener );
		}

		public void prologue() {
			this.isWorking = true;
			this.data = edu.cmu.cs.dennisc.java.util.Collections.newArrayList();
			this.fireContentsChanged();
		}

		public void addAll( java.util.List<java.io.File> files ) {
			this.data.addAll( files );
			this.fireContentsChanged();
		}

		public void done( java.io.File[] data ) {
			//todo: check
			this.isWorking = false;
		}

		private void fireContentsChanged() {
			javax.swing.event.ListDataEvent e = new javax.swing.event.ListDataEvent( this, javax.swing.event.ListDataEvent.CONTENTS_CHANGED, -1, -1 );
			for( javax.swing.event.ListDataListener listDataListener : listDataListeners ) {
				listDataListener.contentsChanged( e );
			}
		}
	}

	private final FilenameComboBoxModel filenameComboBoxModel = new FilenameComboBoxModel();

	private static final java.io.FileFilter PNG_FILE_FILTER = edu.cmu.cs.dennisc.java.io.FileUtilities.createFileWithExtensionFilter( ".png" );
	private static final java.io.FileFilter DIRECTORY_FILTER = edu.cmu.cs.dennisc.java.io.FileUtilities.createDirectoryFilter();

	private class FilenameListWorker extends org.lgna.croquet.worker.WorkerWithProgress<java.io.File[], java.io.File> {
		private final java.io.File rootDirectory;

		public FilenameListWorker( java.io.File rootDirectory ) {
			this.rootDirectory = rootDirectory;
		}

		private void appendDescendants( java.util.List<java.io.File> descendants, java.io.File dir ) {
			java.io.File[] files = dir.listFiles( PNG_FILE_FILTER );
			if( ( files != null ) && ( files.length > 0 ) ) {
				for( java.io.File childFile : files ) {
					descendants.add( childFile );
				}
				this.publish( files );
			}

			java.io.File[] dirs = dir.listFiles( DIRECTORY_FILTER );
			if( dirs != null ) {
				for( java.io.File childDir : dirs ) {
					this.appendDescendants( descendants, childDir );
				}
			}
		}

		@Override
		protected java.io.File[] do_onBackgroundThread() throws Exception {
			if( this.rootDirectory.isDirectory() ) {
				java.util.List<java.io.File> descendants = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
				this.appendDescendants( descendants, this.rootDirectory );
				return edu.cmu.cs.dennisc.java.lang.ArrayUtilities.createArray( descendants, java.io.File.class );
			} else {
				return new java.io.File[ 0 ];
			}
		}

		@Override
		protected void handleDone_onEventDispatchThread( java.io.File[] value ) {
			filenameComboBoxModel.done( value );
		}

		@Override
		protected void handleProcess_onEventDispatchThread( java.util.List<java.io.File> files ) {
			filenameComboBoxModel.addAll( files );
		}
	}

	private FilenameListWorker worker;

	private final SaveOperation saveOperation = new SaveOperation( this );

	private final org.lgna.croquet.State.ValueListener<String> rootDirectoryListener = new org.lgna.croquet.State.ValueListener<String>() {
		public void changing( org.lgna.croquet.State<String> state, String prevValue, String nextValue, boolean isAdjusting ) {
		}

		public void changed( org.lgna.croquet.State<String> state, String prevValue, String nextValue, boolean isAdjusting ) {
			java.io.File file = new java.io.File( nextValue );
			if( file.isDirectory() ) {
				if( worker != null ) {
					if( worker.rootDirectory.equals( file ) ) {
						//pass
						edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "equal", worker.rootDirectory, file );
					} else {
						if( worker.isDone() ) {
							//pass
						} else {
							edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "cancel" );
							worker.cancel( true );
						}
						worker = null;
					}
				}
				if( worker != null ) {
					//pass
				} else {
					filenameComboBoxModel.prologue();
					worker = new FilenameListWorker( file );
					worker.execute();
				}
			}
		}
	};

	//todo
	private final javax.swing.JComboBox jComboBox = new javax.swing.JComboBox( this.filenameComboBoxModel );

	private final javax.swing.event.DocumentListener editorListener = new javax.swing.event.DocumentListener() {
		public void changedUpdate( javax.swing.event.DocumentEvent e ) {
			handleEditorChanged( e );
		}

		public void insertUpdate( javax.swing.event.DocumentEvent e ) {
			handleEditorChanged( e );
		}

		public void removeUpdate( javax.swing.event.DocumentEvent e ) {
			handleEditorChanged( e );
		}
	};

	public ImageEditorFrame() {
		super( java.util.UUID.fromString( "19b37463-3d9a-44eb-9682-6d5ddf73f5b3" ), org.lgna.croquet.Application.DOCUMENT_UI_GROUP ); //todo?
		this.jComboBox.setEditable( true );
		this.saveOperation.setEnabled( false );
	}

	public org.lgna.croquet.ValueHolder<java.awt.Image> getImageHolder() {
		return this.imageHolder;
	}

	public org.lgna.croquet.Operation getClearOperation() {
		return this.clearOperation;
	}

	public org.lgna.croquet.StringState getRootDirectoryState() {
		return this.rootDirectoryState;
	}

	public javax.swing.JComboBox getJComboBox() {
		return this.jComboBox;
	}

	public org.lgna.croquet.Operation getBrowseOperation() {
		return this.browseOperation;
	}

	public org.lgna.croquet.Operation getSaveOperation() {
		return this.saveOperation;
	}

	//	private void copyImageToClipboard( java.awt.Image image ) {
	//		if( image != null ) {
	//			edu.cmu.cs.dennisc.java.awt.datatransfer.ClipboardUtilities.setClipboardContents( image, 300 );
	//		}
	//	}

	public void addShape( java.awt.Shape shape ) {
		this.shapes.add( shape );
		//this.copyImageToClipboard( this.getView().render() );
	}

	public void removeShape( java.awt.Shape shape ) {
		this.shapes.remove( shape );
		//this.copyImageToClipboard( this.getView().render() );
	}

	public void clearShapes() {
		this.shapes.clear();
		//this.copyImageToClipboard( this.imageHolder.getValue() );
	}

	public java.util.List<java.awt.Shape> getShapes() {
		return java.util.Collections.unmodifiableList( this.shapes );
	}

	@Override
	protected org.alice.imageeditor.croquet.views.ImageEditorPane createView() {
		return new org.alice.imageeditor.croquet.views.ImageEditorPane( this );
	}

	public void setImageClearShapesAndShowFrame( java.awt.Image image ) {
		this.clearShapes();
		this.imageHolder.setValue( image );
		this.getBooleanState().setValueTransactionlessly( true );
		org.lgna.croquet.components.AbstractWindow<?> window = this.getView().getRoot();
		if( window != null ) {
			window.pack();
		}
	}

	/* package-private */java.io.File getFile() {
		Object selectedItem = this.filenameComboBoxModel.getSelectedItem();
		if( selectedItem instanceof java.io.File ) {
			java.io.File file = (java.io.File)selectedItem;
			return file;
		} else {
			edu.cmu.cs.dennisc.java.util.logging.Logger.outln( selectedItem );
			return null;
		}
	}

	private void handleEditorChanged( javax.swing.event.DocumentEvent e ) {
		javax.swing.text.Document document = e.getDocument();
		String text = edu.cmu.cs.dennisc.javax.swing.DocumentUtilities.getText( document );
		if( text.length() > 0 ) {
			java.io.File f = new java.io.File( text );
			if( f.isFile() ) {
				this.saveOperation.setName( "save over..." );
			} else {
				this.saveOperation.setName( "save" );
			}
			this.saveOperation.setEnabled( true );
		} else {
			this.saveOperation.setName( "save" );
			this.saveOperation.setEnabled( false );
		}
	}

	@Override
	public void handlePreActivation() {
		this.rootDirectoryState.addAndInvokeValueListener( this.rootDirectoryListener );
		java.awt.Component awtEditorComponent = this.getJComboBox().getEditor().getEditorComponent();
		if( awtEditorComponent instanceof javax.swing.JTextField ) {
			javax.swing.JTextField jTextField = (javax.swing.JTextField)awtEditorComponent;
			jTextField.getDocument().addDocumentListener( this.editorListener );
		}
		super.handlePreActivation();
	}

	@Override
	public void handlePostDeactivation() {
		super.handlePostDeactivation();
		java.awt.Component awtEditorComponent = this.getJComboBox().getEditor().getEditorComponent();
		if( awtEditorComponent instanceof javax.swing.JTextField ) {
			javax.swing.JTextField jTextField = (javax.swing.JTextField)awtEditorComponent;
			jTextField.getDocument().removeDocumentListener( this.editorListener );
		}
		this.rootDirectoryState.removeValueListener( this.rootDirectoryListener );
	}

	public static void main( String[] args ) throws Exception {
		javax.swing.UIManager.LookAndFeelInfo lookAndFeelInfo = edu.cmu.cs.dennisc.javax.swing.plaf.PlafUtilities.getInstalledLookAndFeelInfoNamed( "Nimbus" );
		if( lookAndFeelInfo != null ) {
			javax.swing.UIManager.setLookAndFeel( lookAndFeelInfo.getClassName() );
		}

		final javax.swing.ImageIcon icon = new javax.swing.ImageIcon( org.alice.ide.help.views.HelpView.class.getResource( "images/help.png" ) );
		org.lgna.croquet.simple.SimpleApplication app = new org.lgna.croquet.simple.SimpleApplication();
		final ImageEditorFrame imageComposite = new ImageEditorFrame();
		javax.swing.SwingUtilities.invokeLater( new Runnable() {
			public void run() {
				imageComposite.setImageClearShapesAndShowFrame( icon.getImage() );
			}
		} );
	}
}

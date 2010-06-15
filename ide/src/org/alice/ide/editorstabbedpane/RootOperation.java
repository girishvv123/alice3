/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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
package org.alice.ide.editorstabbedpane;

//class EditFieldOperation extends org.alice.ide.operations.AbstractActionOperation {
//	private edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field;
//
//	public EditFieldOperation( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field ) {
//		this.field = field;
//		this.putValue( javax.swing.Action.NAME, "Edit " + this.field.getName() + "..." );
//	}
//	public void perform( zoot.ActionContext actionContext ) {
//	}
//}

abstract class EditMembersOperation< E extends edu.cmu.cs.dennisc.alice.ast.MemberDeclaredInAlice > extends edu.cmu.cs.dennisc.croquet.InputDialogOperation {
	private edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice declaringType;
	private String presentation;
	public EditMembersOperation( java.util.UUID individualId, edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice declaringType, String presentation ) {
		super( edu.cmu.cs.dennisc.alice.Project.GROUP, individualId );
		this.declaringType = declaringType;
		this.presentation = presentation;
		this.setName( this.presentation + "..." );
	}
	protected abstract EditMembersPane< E > createEditMembersPane( edu.cmu.cs.dennisc.croquet.Group group, edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice declaringType );
	
	private edu.cmu.cs.dennisc.history.HistoryManager historyManager;
	@Override
	protected edu.cmu.cs.dennisc.croquet.Component<?> prologue(edu.cmu.cs.dennisc.croquet.ModelContext context) {
		edu.cmu.cs.dennisc.croquet.Group group = new edu.cmu.cs.dennisc.croquet.Group( java.util.UUID.randomUUID(), "EDIT_MEMBERS_GROUP: " + context );
		this.historyManager = edu.cmu.cs.dennisc.history.HistoryManager.getInstance( group );
		return this.createEditMembersPane( group, this.declaringType );
	}
	@Override
	protected void epilogue(edu.cmu.cs.dennisc.croquet.ModelContext context, boolean isOk) {
		if( isOk ) {
			edu.cmu.cs.dennisc.croquet.Edit compositeEdit = historyManager.createDoIgnoringCompositeEdit( this.presentation + " " + this.declaringType.getName() );
			if( compositeEdit != null ) {
				context.commitAndInvokeDo( compositeEdit );
			} else {
				//todo?
				context.cancel();
			}
		} else {
			this.historyManager.setInsertionIndex( 0 );
			context.cancel();
		}
	}
}

abstract class EditMethodsOperation< E extends edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice > extends EditMembersOperation< E > {
	public EditMethodsOperation( java.util.UUID individualId, edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type, String presentation ) {
		super( individualId, type, presentation );
	}
}
class EditProceduresOperation extends EditMethodsOperation< edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice > {
	public EditProceduresOperation( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type ) {
		super( java.util.UUID.fromString( "f3160b4c-d060-4e60-839c-3dfd761f71ce" ), type, "Edit Procedures" );
	}
	@Override
	protected EditProceduresPane createEditMembersPane( edu.cmu.cs.dennisc.croquet.Group group, edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice declaringType ) {
		return new EditProceduresPane( declaringType );
	}
}
class EditFunctionsOperation extends EditMethodsOperation< edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice > {
	public EditFunctionsOperation( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type ) {
		super( java.util.UUID.fromString( "f911b1ae-3ceb-4749-99de-dee5f40a8109" ), type, "Edit Functions" );
	}
	@Override
	protected EditFunctionsPane createEditMembersPane( edu.cmu.cs.dennisc.croquet.Group group, edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice declaringType ) {
		return new EditFunctionsPane( declaringType );
	}
}

class EditFieldsOperation extends EditMembersOperation< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > {
	public EditFieldsOperation( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type ) {
		super( java.util.UUID.fromString( "64816bc1-6c0f-4675-bb77-2e53086b2f74" ), type, "Edit Properties" );
	}
	@Override
	protected EditFieldsPane createEditMembersPane( edu.cmu.cs.dennisc.croquet.Group group, edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice declaringType ) {
		return new EditFieldsPane( declaringType );
	}
}

abstract class MembersFillIn extends edu.cmu.cs.dennisc.cascade.MenuFillIn< edu.cmu.cs.dennisc.zoot.ActionOperation > {
	private edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type;

	public MembersFillIn( String text, edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type ) {
		super( text );
		this.type = type;
	}
	protected abstract int getMemberCount();
	@Override
	protected String getLabelText() {
		return super.getLabelText() + " (" + this.getMemberCount() + ")";
	}
	protected edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice getType() {
		return this.type;
	}
}

abstract class MethodsFillIn extends MembersFillIn {
	public MethodsFillIn( String text, edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type ) {
		super( text, type );
	}
	protected abstract boolean isDesired( edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method );
	@Override
	public int getMemberCount() {
		int rv = 0;
		for( edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method : this.getType().methods ) {
			if( this.isDesired( method ) ) {
				rv++;
			}
		}
		return rv;
	}
	@Override
	protected void addChildrenToBlank( edu.cmu.cs.dennisc.cascade.Blank blank ) {
		for( edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method : this.getType().methods ) {
			if( this.isDesired( method ) ) {
				blank.addFillIn( new OperatorFillIn( new EditMethodOperation( method ) ) );
			}
		}
		blank.addSeparator();
	}
}

class ProceduresFillIn extends MethodsFillIn {
	public ProceduresFillIn( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type ) {
		super( "Procedures", type );
	}
	@Override
	protected boolean isDesired( edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method ) {
		return method.isProcedure();
	}
	@Override
	protected void addChildrenToBlank( edu.cmu.cs.dennisc.cascade.Blank blank ) {
		super.addChildrenToBlank( blank );
		blank.addFillIn( new OperatorFillIn( org.alice.ide.operations.ast.DeclareProcedureOperation.getInstance( this.getType() ) ) );
		blank.addSeparator();
		blank.addFillIn( new OperatorFillIn( new EditProceduresOperation( this.getType() ) ) );
	}
}

class FunctionsFillIn extends MethodsFillIn {
	public FunctionsFillIn( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type ) {
		super( "Functions", type );
	}
	@Override
	protected boolean isDesired( edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method ) {
		return method.isFunction();
	}
	@Override
	protected void addChildrenToBlank( edu.cmu.cs.dennisc.cascade.Blank blank ) {
		super.addChildrenToBlank( blank );
		blank.addFillIn( new OperatorFillIn( org.alice.ide.operations.ast.DeclareFunctionOperation.getInstance( this.getType() ) ) );
		blank.addSeparator();
		blank.addFillIn( new OperatorFillIn( new EditFunctionsOperation( this.getType() ) ) );
	}
}

class FieldsFillIn extends MembersFillIn {
	public FieldsFillIn( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type ) {
		super( "Properties", type );
	}
	@Override
	public int getMemberCount() {
		return this.getType().fields.size();
	}
	@Override
	protected void addChildrenToBlank( edu.cmu.cs.dennisc.cascade.Blank blank ) {
		for( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field : this.getType().fields ) {
			blank.addFillIn( new OperatorFillIn( new org.alice.ide.operations.ast.EditFieldOperation( field ) ) );
		}
		blank.addSeparator();
		blank.addFillIn( new OperatorFillIn( new org.alice.ide.operations.ast.DeclareFieldOperation( this.getType() ) ) );
		blank.addSeparator();
		blank.addFillIn( new OperatorFillIn( new EditFieldsOperation( this.getType() ) ) );
	}
}

class TypeFillIn extends edu.cmu.cs.dennisc.cascade.MenuFillIn< edu.cmu.cs.dennisc.zoot.ActionOperation > {
	private edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type;

	public TypeFillIn( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type ) {
		super( type.getName() );
		this.type = type;
	}
	@Override
	protected javax.swing.JComponent createMenuProxy() {
		return org.alice.ide.common.TypeComponent.createInstance( this.type ).getAwtComponent();
	}
	@Override
	protected void addChildrenToBlank( edu.cmu.cs.dennisc.cascade.Blank blank ) {
		if( this.type != null ) {
			blank.addFillIn( new ProceduresFillIn( this.type ) );
			blank.addFillIn( new FunctionsFillIn( this.type ) );
			blank.addFillIn( new OperatorFillIn( new EditConstructorOperation( this.type.getDeclaredConstructor() ) ) );
			blank.addSeparator();
			blank.addFillIn( new FieldsFillIn( this.type ) );
			blank.addSeparator();
			blank.addFillIn( new OperatorFillIn( new org.alice.ide.operations.ast.RenameTypeOperation( this.type ) ) );
			//blank.addFillIn( new OperatorFillIn( new org.alice.ide.operations.file.SaveAsTypeOperation( this.type ) ) );
		} else {
			blank.addFillIn( new edu.cmu.cs.dennisc.cascade.CancelFillIn( "type is not set.  canceling." ) );
		}
	}
}

class ProjectBlank extends edu.cmu.cs.dennisc.cascade.Blank {
	private edu.cmu.cs.dennisc.alice.Project project;

	public ProjectBlank( edu.cmu.cs.dennisc.alice.Project project ) {
		assert project != null;
		this.project = project;
	}
	@Override
	protected void addChildren() {
		edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice > crawler = new edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice >( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice.class );
		final edu.cmu.cs.dennisc.alice.ast.AbstractType programType = this.project.getProgramType();
		programType.crawl( crawler, true );
		for( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type : crawler.getList() ) {
			if( type == programType ) {
				//pass
			} else {
				this.addFillIn( new TypeFillIn( type ) );
			}
		}
	}
}

/**
 * @author Dennis Cosgrove
 */
public class RootOperation extends org.alice.ide.operations.InconsequentialActionOperation {
	public RootOperation() {
		super( java.util.UUID.fromString( "259dfcc5-dd20-4890-8104-a34a075734d0" ) );
	}
	@Override
	protected void performInternal( edu.cmu.cs.dennisc.croquet.ActionOperationContext context ) {
		edu.cmu.cs.dennisc.croquet.ViewController<?,?> viewController = context.getViewController();
		int x = 0;
		int y = viewController.getHeight();
		edu.cmu.cs.dennisc.alice.Project project = getIDE().getProject();
		if( project != null ) {
			new ProjectBlank( project ).showPopupMenu( viewController.getAwtComponent(), x, y, new edu.cmu.cs.dennisc.task.TaskObserver< edu.cmu.cs.dennisc.croquet.Operation< ?,? > >() {
				public void handleCompletion( edu.cmu.cs.dennisc.croquet.Operation< ?,? > operation ) {
					operation.fire();
				}
				public void handleCancelation() {
				}
			} );
		} else {
			this.getIDE().showMessageDialog( "Open a project first (via the File Menu)", "No Project", edu.cmu.cs.dennisc.croquet.MessageType.INFORMATION );
		}
	}
}

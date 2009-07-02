/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package edu.cmu.cs.dennisc.jira.soap;

/**
 * @author Dennis Cosgrove
 */
public class SOAPUtilities {
	private static com.atlassian.jira.rpc.soap.client.RemoteCustomFieldValue createCustomField( int key, String value ) {
		return new com.atlassian.jira.rpc.soap.client.RemoteCustomFieldValue( "customfield_" + key, "", new String[] { value } );
	}
	private static com.atlassian.jira.rpc.soap.client.RemoteVersion[] getRemoteAffectsVersions( edu.cmu.cs.dennisc.jira.JIRAReport jiraReport, com.atlassian.jira.rpc.soap.client.JiraSoapService service, String token, String project ) throws java.rmi.RemoteException {
		String[] affectsVersions = jiraReport.getAffectsVersions();
		if( affectsVersions != null && affectsVersions.length > 0 ) {
			String affectsVersion = affectsVersions[ 0 ];
			com.atlassian.jira.rpc.soap.client.RemoteVersion[] versions = service.getVersions( token, project );
			for( com.atlassian.jira.rpc.soap.client.RemoteVersion version : versions ) {
				String versionName = version.getName();
				if( versionName != null && versionName.length() > 0 ) {
					if( versionName.equals( affectsVersion ) ) {
						return new com.atlassian.jira.rpc.soap.client.RemoteVersion[] { version };
					}
				}
			}
		}
		return new com.atlassian.jira.rpc.soap.client.RemoteVersion[] {};
	}
//	private static StringBuffer appendSystemProperty( StringBuffer rv, String key ) {
//		rv.append( key );
//		rv.append( ": " );
//		rv.append( System.getProperty( key ) );
//		rv.append( ";\n" );
//		return rv;
//	}
	private static com.atlassian.jira.rpc.soap.client.RemoteIssue createPreparedIssue( edu.cmu.cs.dennisc.jira.JIRAReport jiraReport ) {
		com.atlassian.jira.rpc.soap.client.RemoteIssue remoteIssue = new com.atlassian.jira.rpc.soap.client.RemoteIssue();
		remoteIssue.setSummary( edu.cmu.cs.dennisc.jira.JIRAUtilities.ensureStringWithinLimit( jiraReport.getSummary(), 254 ) );
		remoteIssue.setType( Integer.toString( edu.cmu.cs.dennisc.jira.JIRAUtilities.getType( jiraReport.getType() ) ) );
		remoteIssue.setDescription( jiraReport.getDescription() );
		
		com.atlassian.jira.rpc.soap.client.RemoteCustomFieldValue steps = createCustomField( 10000, jiraReport.getSteps() );
		com.atlassian.jira.rpc.soap.client.RemoteCustomFieldValue exception = createCustomField( 10001, jiraReport.getException() );
		remoteIssue.setCustomFieldValues( new com.atlassian.jira.rpc.soap.client.RemoteCustomFieldValue[]{ steps, exception } );
		
		StringBuffer environment = new StringBuffer();
		String[] affectsVersions = jiraReport.getAffectsVersions();
		if( affectsVersions != null && affectsVersions.length > 0 ) {
			environment.append( "version: " );
			environment.append( affectsVersions[ 0 ] );
			environment.append( ";\n" );
		}
//		SOAPUtilities.appendSystemProperty( environment, "os.name" );
//		SOAPUtilities.appendSystemProperty( environment, "java.vm.version" );
//		if( isInclusionOfCompleteSystemPropertiesDesired ) {
//			environment.append( "complete system properties:\n" );
//			environment.append( edu.cmu.cs.dennisc.lang.SystemUtilities.getPropertiesAsXMLString() );
//			environment.append( "\n;\n" );
//		}
//		remoteIssue.setEnvironment( environment.toString() );
		remoteIssue.setEnvironment( jiraReport.getEnvironment() );
		return remoteIssue;
	}

	public static com.atlassian.jira.rpc.soap.client.RemoteIssue createIssue( edu.cmu.cs.dennisc.jira.JIRAReport jiraReport, com.atlassian.jira.rpc.soap.client.JiraSoapService service, String token ) throws java.rmi.RemoteException {
		String project = jiraReport.getProjectKey();
		com.atlassian.jira.rpc.soap.client.RemoteIssue remoteIssue = createPreparedIssue( jiraReport );
		remoteIssue.setProject( project );
		com.atlassian.jira.rpc.soap.client.RemoteVersion[] remoteAffectsVersions = SOAPUtilities.getRemoteAffectsVersions( jiraReport, service, token, project );
		remoteIssue.setAffectsVersions( remoteAffectsVersions );
		com.atlassian.jira.rpc.soap.client.RemoteIssue rv = service.createIssue( token, remoteIssue );
		
		java.util.List< edu.cmu.cs.dennisc.issue.Attachment > attachments = jiraReport.getAttachments();
		if( attachments != null && attachments.size() > 0 ) {
			try {
				final int N = attachments.size();
				String[] names = new String[ N ];
				byte[][] data = new byte[ N ][];
				for( int i=0; i<N; i++ ) {		
					edu.cmu.cs.dennisc.issue.Attachment attachmentI = attachments.get( i );
					names[ i ] = attachmentI.getFileName();
					data[ i ] = attachmentI.getBytes();
				}
				service.addAttachmentsToIssue( token, rv.getKey(), names, data );
			} catch( Exception e ) {
				e.printStackTrace();
			}
		}
		return rv;
	}
}

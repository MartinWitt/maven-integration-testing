package org.apache.maven.it;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.apache.maven.it.Verifier;
import org.apache.maven.it.util.ResourceExtractor;

import java.io.File;

/**
 * This is a test set for <a href="http://jira.codehaus.org/browse/MNG-4335">MNG-4335</a>.
 * 
 * @author Benjamin Bentmann
 */
public class MavenITmng4335SettingsOfflineModeTest
    extends AbstractMavenIntegrationTestCase
{

    public MavenITmng4335SettingsOfflineModeTest()
    {
        super( ALL_MAVEN_VERSIONS );
    }

    /**
     * Test that offline mode is enabled when specified in the settings.xml
     */
    public void testit()
        throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-4335" );

        Verifier verifier = new Verifier( testDir.getAbsolutePath() );
        verifier.setAutoclean( false );
        verifier.deleteDirectory( "target" );
        verifier.deleteArtifacts( "org.apache.maven.its.mng4335" );
        verifier.getCliOptions().add( "-s" );
        verifier.getCliOptions().add( "settings.xml" );
        verifier.filterFile( "settings-template.xml", "settings.xml", "UTF-8", verifier.newDefaultFilterProperties() );
        try
        {
            verifier.executeGoal( "validate" );
            verifier.verifyErrorFreeLog();
            fail( "Build did not fail to resolve missing dependency although Maven ought to work offline!" );
        }
        catch( VerificationException e )
        {
            // expected, should fail
        }
        verifier.resetStreams();
    }

}
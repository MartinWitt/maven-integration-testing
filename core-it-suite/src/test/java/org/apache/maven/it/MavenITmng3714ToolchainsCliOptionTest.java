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
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * This is a test set for <a href="http://jira.codehaus.org/browse/MNG-3714">MNG-3714</a>.
 * 
 * @author Brett Porter
 * @version $Id: MavenITmng0557UserSettingsCliOptionTest.java 744504 2009-02-14 14:49:27Z brett $
 */
public class MavenITmng3714ToolchainsCliOptionTest
    extends AbstractMavenIntegrationTestCase
{
    public MavenITmng3714ToolchainsCliOptionTest()
    {
        super( "[2.2.0,)" );
    }

    /**
     * Test --toolchains CLI option
     */
    public void testitMNG3714()
        throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-3714" );
        File javaHome = new File( testDir, "javaHome" );
        javaHome.mkdirs();
        
        Verifier verifier = new Verifier( testDir.getAbsolutePath() );
        Properties properties = verifier.newDefaultFilterProperties();
        properties.setProperty( "@javaHome@", javaHome.getAbsolutePath() );

        verifier.filterFile( "toolchains.xml", "toolchains.xml", "UTF-8", properties );

        verifier.setAutoclean( false );
        verifier.deleteDirectory( "target" );
        List cliOptions = new ArrayList();
        cliOptions.add( "--toolchains toolchains.xml" );
        verifier.setCliOptions( cliOptions );
        verifier.executeGoal( "org.apache.maven.its.plugins:maven-it-plugin-toolchain:toolchain" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

        verifier.assertFilePresent( "target/toolchains.properties" );
        Properties results = verifier.loadProperties( "target/toolchains.properties" );
        String tool = results.getProperty( "tool.1" );
        if ( tool.endsWith( ".exe" ) )
        {
            tool = tool.substring( 0, tool.length() - 4 );
        }
        assertEquals( new File( javaHome, "bin/javac" ).getAbsolutePath(), tool );
    }

}
/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2010 Sun Microsystems, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 *
 * Contributor(s):
 *
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 *
 */
package org.glassfish.tests.kernel.admin;

import com.sun.enterprise.module.ModulesRegistry;
import com.sun.enterprise.module.bootstrap.StartupContext;
import com.sun.enterprise.module.single.SingleModulesRegistry;
import com.sun.hk2.component.ExistingSingletonInhabitant;
import junit.framework.Assert;
import org.glassfish.api.ActionReport;
import org.glassfish.api.admin.CommandRunner;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvnet.hk2.annotations.Inject;
import org.jvnet.hk2.component.Habitat;
import org.jvnet.hk2.junit.Hk2Runner;

/**
 * Test the command runner implementation.
 *
 * @author Jerome Dochez
 */
@RunWith(Hk2Runner.class)
//@Ignore
public class CommandRunnerTest {

    @Inject
    CommandRunner commandRunner;

    @BeforeClass
    public static void setup() {
        Habitat h = Hk2Runner.getHabitat();
        h.addComponent(null, new StartupContext());
        h.addIndex(new ExistingSingletonInhabitant<ModulesRegistry>(new SingleModulesRegistry(CommandRunnerTest.class.getClassLoader()))
                , ModulesRegistry.class.getName(), null);
    }

    @Test
    public void tryOut() {
        Assert.assertTrue(commandRunner!=null);
        try {
            ActionReport report = commandRunner.getActionReport("plain");
            CommandRunner.CommandInvocation inv = commandRunner.getCommandInvocation("list-contracts", report);
            inv.execute();
            System.out.println(report.getTopMessagePart().getMessage());
            for (ActionReport.MessagePart child : report.getTopMessagePart().getChildren()) {
                System.out.println(child.getMessage());
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}

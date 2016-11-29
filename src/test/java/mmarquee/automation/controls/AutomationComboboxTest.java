/*
 * Copyright 2016 inpwtepydjuf@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mmarquee.automation.controls;

import mmarquee.automation.AutomationException;
import mmarquee.automation.UIAutomation;
import mmarquee.automation.pattern.PatternNotFoundException;
import mmarquee.automation.uiautomation.IUIAutomationTest;
import org.apache.log4j.Logger;
import org.junit.Test;
import java.util.List;
import static java.awt.SystemColor.text;
import static org.junit.Assert.assertTrue;

/**
 * Created by Mark Humphreys on 29/11/2016.
 */
public class AutomationComboboxTest {

    protected Logger logger = Logger.getLogger(IUIAutomationTest.class.getName());

    static {
        ClassLoader.getSystemClassLoader().setDefaultAssertionStatus(true);
    }

    protected void rest() {
        try {
            Thread.sleep(1500);
        } catch (Exception ex) {
            logger.info("Interrupted");
        }
    }

    private AutomationApplication application = null;
    private AutomationWindow applicationWindow = null;

    private void loadApplication(String appName, String windowName) throws Exception {
        this.rest();

        UIAutomation automation = UIAutomation.getInstance();

        application = automation.launchOrAttach(appName);

        // Wait for the process to start
        // This doesn't seem to wait for WPF examples
        application.waitForInputIdle(5000);

        // Sleep for WPF, to address above issue
        this.rest();

        applicationWindow = automation.getDesktopWindow(windowName);
    }

    private void closeApplication() throws PatternNotFoundException, AutomationException {
        application.quit("Form1");
    }

    @Test
    public void testGetCombobox_Get_Name() throws Exception {
        loadApplication("apps\\Project1.exe", "Form1");

        try {
            AutomationComboBox cb1 = applicationWindow.getCombobox(0);

            String name = cb1.name();

            logger.info(name);

            assertTrue(name.equals("AutomatedCombobox2"));
        } finally {
            closeApplication();
        }
    }

    @Test
    public void testGetCombobox_Get_Text() throws Exception {
        loadApplication("apps\\Project1.exe", "Form1");

        try {
            AutomationComboBox cb1 = applicationWindow.getCombobox(0);

            String text = cb1.text();

            logger.info(text);

            assertTrue(text.equals("Second"));
        } finally {
            closeApplication();
        }
    }

    @Test
    public void testGetCombobox_Get_List_Gets_Correct_Size_Of_List() throws Exception {
        loadApplication("apps\\Project1.exe", "Form1");

        try {
            AutomationComboBox cb1 = applicationWindow.getCombobox(0);

            List<AutomationListItem> elements = cb1.getList();

            int count = elements.size();

            logger.info(count);

            assertTrue(count == 0);
        } finally {
            closeApplication();
        }
    }

    @Test
    public void testGetCombobox_Get_List_Gets_Correct_Size_Of_List_When_Expanded() throws Exception {
        loadApplication("apps\\Project1.exe", "Form1");

        try {
            AutomationComboBox cb1 = applicationWindow.getCombobox(0);

            cb1.expand();

            List<AutomationListItem> elements = cb1.getList();

            int count = elements.size();

            logger.info(count);

            assertTrue(count == 3);
        } finally {
            closeApplication();
        }
    }

}

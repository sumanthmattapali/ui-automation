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
package mmarquee.automation;

import com.sun.jna.platform.win32.WinDef;
import mmarquee.automation.controls.*;
import mmarquee.automation.controls.menu.AutomationMainMenu;
import mmarquee.automation.uiautomation.ToggleState;
import org.apache.log4j.Logger;

/**
 * Created by inpwt on 26/02/2016.
 */
public class TestMainWPF {

    public void run() {
        UIAutomation automation = new UIAutomation();

        Logger logger = Logger.getLogger(AutomationBase.class.getName());

        AutomationApplication application = null;

        try {
            application = automation.launchOrAttach("apps\\SampleWpfApplication.exe");
        } catch (Throwable ex) {
            // Smother????
            logger.warn("Failed to find application", ex);
        }

        // Wait for the process to start
        application.waitForInputIdle(5000);

        try {
            AutomationWindow window = automation.getDesktopWindow("MainWindow");
            String name = window.name();
            logger.info(name);

            boolean val = window.isModal();

            Object rect = window.getBoundingRectangle();

            WinDef.HWND handle = window.getNativeWindowHandle();

            // Interact with menus
            AutomationMainMenu menu = window.getMainMenu(0);

//            AutomationMainMenu menu = window.getMenu();   // WPF menus seem to be different from Delphi VCL windows

            // Not menus for now

//            try {
//                AutomationMenuItem exit = menu.getMenuItem("File", "Exit");
//                exit.click();
/*
                AutomationWindow popup = window.getWindow("Project1");
                Object val111 = popup.getBoundingRectangle();

                AutomationButton btn = popup.getButton("OK");
                Object val11 = btn.getBoundingRectangle();

                boolean val1 = popup.isModal();

                btn.click();
                */
//            } catch (ElementNotFoundException ex) {
//                logger.info("Failed to find menu");
//            }

            // Get and set an edit box by index (WPF doesn't care about control names)

            AutomationTab tab = window.getTab(0);
            tab.selectTabPage("Details");

            String text = window.getEditBox(1).getValue();
            logger.info("Text for edit box 1 is " + text);

            window.getEditBox(1).setValue("Hi");
            logger.info("Text for edit box 1 is now " + window.getEditBox(1).getValue());

            // CHECK BOX *********************************************

            AutomationCheckbox check = window.getCheckbox(0);
            check.toggle();
            ToggleState state = check.getToggleState();

            // RADIO BUTTON *********************************************

            AutomationRadioButton radio = window.getRadioButton(1);
            radio.selectItem();

            // TEXT BOX *********************************************

            AutomationTextBox tb0 = window.getTextBox(9);
            String tb0Text = tb0.getValue();
            logger.info("Text for text box 1 is " + tb0Text);

            AutomationTextBox tb1 = window.getTextBox(18);
            String tb1Text = tb1.getValue();
            logger.info("Text for text box 1 is " + tb1Text);

            // PROGRESS BAR *********************************************

            AutomationProgressBar progress = window.getProgressBar(0);
            logger.info("Progress = " + progress.getRangeValue());

            // Looks like this does bad things
          //  progress.setRangeValue(100.0);
          //  logger.info("Progress is now = " + progress.getRangeValue());

            // SLIDER *********************************************

            AutomationSlider slider = window.getSlider(0);
            logger.info("Slider value = " + slider.getRangeValue());

            // Looks like this does bad things too
     //       progress.setRangeValue(25);
     //       logger.info("Progress is now = " + progress.getRangeValue());

            // Status bar *********************************************

            AutomationStatusBar statusbar = window.getStatusBar();

            AutomationTextBox tb = statusbar.getTextBox(0);

            String ebText = tb.getValue();

            logger.info("Statusbar text = " + ebText);

            // Now make something happen in the statusbar
            AutomationEditBox sbeb = window.getEditBox(0);
            logger.info(sbeb.getValue());
            sbeb.setValue("Some text");

            logger.info("Statusbar text = " + tb.getValue());

            // COMBOBOX *********************************************

            try {
                AutomationComboBox cb0 = window.getCombobox(0);

// NPE thrown here
//                String txt = cb0.text();
//                logger.info("Text for Combobox is `" + txt + "`");
            } catch (ElementNotFoundException ex) {
                logger.error("Failed to find element");
            }

            // EDITTABLE COMBOBOX ************************************

            try {
                AutomationComboBox cb1 = window.getCombobox(1);

                String txt = cb1.text();

                logger.info("Text for Combobox is `" + txt + "`");

                cb1.setText("Here we are");
                logger.info("Text for Combobox is now `" + cb1.text() + "`");

            } catch (ElementNotFoundException ex) {
                logger.error("Failed to find element");
            }


            // MASKED EDIT ****************************************
/*
            try {
                AutomationMaskedEdit me0 = window.getMaskedEdit("AutomatedMaskEdit1");

                String value = me0.getValue();
                logger.info("Initial value " + value);

                me0.setValue("12/12/99");

                String value1 = me0.getValue();
                logger.info("Changed value is " + value1);

            } catch (ElementNotFoundException ex) {
                logger.error("Failed to find maskededit");
            }
*/

            // DATAGRIDS ***********************************************************

            // These are entirely different beasts in WPF, but look the same to us!

            // Now string grids
            AutomationDataGrid grid = window.getDataGrid(0);

            AutomationDataGridCell cell1 = grid.getItem(1, 1);

            String itemName = cell1.name();
            logger.info("Grid item is " + itemName);
            cell1.setName("This");
            logger.info("Grid item is " + cell1.name());

            // TREEVIEW **************************

            AutomationTreeView tree = window.getTreeView(0);
            try {
                AutomationTreeViewItem treeItem = tree.getItem("Level 2.2");
                treeItem.select();

                logger.info("Item is " + treeItem.name());

            } catch (ItemNotFoundException ex) {
                logger.info("Failed to find item");
            } catch (ElementNotFoundException ex) {
                logger.info("Failed to find element");
            }

            // BUTTONS ***********************************

            // NOTE: WPF buttons will set the automationID to be the name of the control

            AutomationButton btn = window.getButtonByAutomationId("btnClickMe");
            logger.info(btn.name());
            btn.click();

            // LISTS ****************************************

            AutomationList list = window.getListItem(0);
            try {
                AutomationListItem listItem = list.getItem("Hello, Window world!");
                listItem.select();
                logger.info(listItem.name());

                // Now find by index
                AutomationListItem listItem0 = list.getItem(0);
                listItem0.select();
                logger.info("0th element is " + listItem0.name());

            } catch (ItemNotFoundException ex) {
                logger.info("Didn't find item");
            } catch (ElementNotFoundException ex) {
                logger.info("Didn't find element");
            }

            // HYPERLINK ***********************************

            AutomationHyperlink link = window.getHyperlink(0);
            link.click();

            AutomationToolBar toolbar = window.getToolBar(0);
            logger.info("Toolbar name is " + toolbar.name()); // Blank in default WPF

            AutomationButton btn1 = toolbar.getButton(1);

            if (btn1.isEnabled()) {
                logger.info("btn0 Enabled");
                logger.info(btn1.name());
                btn1.click();

                // Now cope with the results of the click
                AutomationWindow popup = window.getWindow("New Thing");

                AutomationButton okBtn = popup.getButton("OK");

                boolean val1 = popup.isModal();

                logger.info("Modal - " + val1);

                okBtn.click();
            }

            // CALENDAR ***********************************

            tab.selectTabPage("Calendar");

            AutomationCalendar calendar = window.getCalendar(0);

            logger.info("Date is " + calendar.name());

            // Not sure what we can get out of a calendar

            // DOCUMENT *********************************************

            tab.selectTabPage("Document");

            AutomationDocument document = window.getDocument(0);

            logger.info("Document name is " + document.name());

            // TITLEBAR ****************************************

            AutomationTitleBar titleBar = window.getTitleBar();
            logger.info("TitleBar name is " + titleBar.name());

            AutomationMainMenu menuBar = titleBar.getMenuBar();

            AutomationButton btnMin = titleBar.getButton(0);
            AutomationButton btnMax = titleBar.getButton(1);
            AutomationButton btnClose = titleBar.getButton(2);

            logger.info(btnMin.name());
            logger.info(btnMax.name());
            logger.info(btnClose.name());


        } catch (ElementNotFoundException ex) {
            logger.info("Element Not Found ");
        }
    }
}
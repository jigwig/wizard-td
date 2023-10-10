package WizardTD;

import processing.core.PApplet;

public class Buttons {

    private App app;
    private int x, y, width, height;
    private String label;
    private String description;
    private char hotkey;
    private int cost;
    private boolean isHovered = false;
    private boolean isActive = false;

    public Buttons(App app, int x, int y, String label, String description, char hotkey, int cost) {
        this.app = app;
        this.x = x;
        this.y = y;
        this.label = label;
        this.description = description;
        this.hotkey = hotkey;
        this.cost = cost;
        this.width = 30; //  width for now
        this.height = 30; // arbitrary height for now
    }

    public void draw() {
        if (isActive)
        {
            app.fill(255, 255, 0);
        } else if (isHovered) {
            app.fill(200, 200, 200); // Fill gray when hovered
        } else {
            app.noFill();
        }

        app.rect(x, y, width, height);
    
        // Set text size and bold style
        app.textSize(16); // Change the size as per your preference
        app.fill(0); // Black text
        app.text(label, x + 3, y + height / 2); // Offset by 10 pixels from left
        app.textSize(10);
        app.text(description, x + width + 5, y, 50, 80); // Description on the right
    
        if (isHovered && cost > 0) {
            app.text("Cost: " + cost, x + width / 2, y + height + 15); // Display cost below button
        }
    }
    
    public void update(int mouseX, int mouseY) {
        isHovered = mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
    }

    public void onClick(int mouseX, int mouseY) {
        if (isHovered) {
            isActive = !isActive; // Toggle active state
    
            // Implementing the features
            if ("FF".equals(this.label)) {
                app.isFastForwarded = isActive;
            }
            else if ("P".equals(this.label)) {
                app.isPaused = isActive;
            }
        }
    }
    

    public boolean isActive()
    {
        return isActive;
    }

    public boolean matchesHotkey(char key)
    {
        return key == hotkey;
    }

    public void onKey(char key) {
        if (this.hotkey == Character.toLowerCase(key)) {
            isActive = !isActive;

            // Implementing the features
            if ("FF".equals(this.label)) {
                app.isFastForwarded = isActive; 

            }
            else if ("P".equals(this.label)) {
                app.isPaused = isActive; 

            }
        }
    }
}
    
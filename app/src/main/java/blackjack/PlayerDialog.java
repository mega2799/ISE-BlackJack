package blackjack;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import blackjack.Players.Player;


public class PlayerDialog extends JDialog implements ActionListener
{
    String name;
    int age;
    String gender;
    
    Player player;
    
    String[] genders = {"Male", "Female"};
    
    JTextField playerName = new JTextField();
    JTextField playerAge = new JTextField();
    JComboBox playerGender = new JComboBox(this.genders);
     
    public PlayerDialog(final Frame owner, final String title, final boolean modal, final Player player)
    {
        super(owner, title, modal);
        this.setSize(300, 200);
        this.setLocationRelativeTo(null);
        
        this.player = player;
        
        final JPanel playerDetailsPanel = new JPanel(new SpringLayout());
        
        final JLabel nameLabel = new JLabel("Name", JLabel.TRAILING);
        playerDetailsPanel.add(nameLabel);
        nameLabel.setLabelFor(this.playerName);
        playerDetailsPanel.add(this.playerName);
        
        final JLabel ageLabel = new JLabel("Age", JLabel.TRAILING);
        playerDetailsPanel.add(ageLabel);
        ageLabel.setLabelFor(this.playerAge);
        playerDetailsPanel.add(this.playerAge);
        
        final JLabel genderLabel = new JLabel("Gender", JLabel.TRAILING);
        playerDetailsPanel.add(genderLabel);
        genderLabel.setLabelFor(this.playerGender);
        playerDetailsPanel.add(this.playerGender);
        
        SpringUtilities.makeCompactGrid(playerDetailsPanel,
                                        3, 2,
                                        5, 5,
                                        5, 5);
        
        this.add(playerDetailsPanel, BorderLayout.NORTH);
        
        final JButton updateButton = new JButton("Update Details");
        final JButton cancelButton = new JButton("Cancel");
        
        final JPanel playerUpdatePanel = new JPanel();
        playerUpdatePanel.add(updateButton);
        playerUpdatePanel.add(cancelButton);
        this.add(playerUpdatePanel, BorderLayout.SOUTH);
        
        updateButton.addActionListener(this);
        cancelButton.addActionListener(this);
        
        this.playerName.setText(player.getName());
        this.playerAge.setText(Integer.toString(player.getAge()));
        this.playerGender.setSelectedItem(player.getGender());
        
    }
    
    public void actionPerformed(final ActionEvent evt)
    {
        final String act = evt.getActionCommand();
        
        if ("Update Details".equals(act))
        {
            this.updateDetails();
        }
        else if ("Cancel".equals(act))
        {
            this.setVisible(false);
            this.dispose();
        }
    }
    
    private void updateDetails()
    {
        boolean validName = true;
        if ("".equals(this.playerName.getText()))
        {
            JOptionPane.showMessageDialog(null, "Please enter a Player name", "Error", JOptionPane.ERROR_MESSAGE);
            validName = false;
        }
        else
        {
            this.player.setName(this.playerName.getText());
        }
        
        boolean validAge = true;
        try
        {
            this.age = Integer.parseInt(this.playerAge.getText());
        }
        catch (final NumberFormatException e)
	    {
	        JOptionPane.showMessageDialog(null, "Please enter an age - numbers only", "Error", JOptionPane.ERROR_MESSAGE);
	        validAge = false;
	    }
	    
	    if (validAge)
	    {
	        this.player.setAge(this.age);
	    }
	    
	    this.player.setGender((String) this.playerGender.getSelectedItem());
	    
	    if (validAge && validName)
	    {
	        this.setVisible(false);
	        this.dispose();
        }
    }
    
    public Player getPlayer()
    {
        return this.player;
    }
}
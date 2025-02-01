package blackjack;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class SimpleFileFilter extends FileFilter
{
    private String extension = "";
    private String description = "";
    
    public SimpleFileFilter()
    {
        
    }
    
    public SimpleFileFilter(final String extension, final String description)
    {
        if (extension != null)
        {
            this.extension = extension;
        }
        
        if (description != null)
        {
            this.description = description;
        }
    }
    
    public boolean accept(final File file)
    {
        if (file != null)
        {
            if (file.isDirectory())
            {
                return false;
            }
        
            final String fileName = file.getName();
            final int i = fileName.lastIndexOf('.');
            if (i > 0 && i < fileName.length() - 1)
            {
                final String end = fileName.substring(i, fileName.length());
                return end.equals(this.extension);
            }
        }
        
        return false;
    }
    
    public String getDescription()
    {
        return this.description;
    }
}
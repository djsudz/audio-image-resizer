/**
 * 
 */
package za.co.djsudz.audio;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileFilter;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.images.StandardArtwork;
import org.jaudiotagger.tag.images.StandardImageHandler;

/**
 * @author Sudheer
 *
 */
public class AudioImageResizer {
	
	public void resizeCoverArt(File srcAudioFile, int size) {
		
		System.out.println("Resizing File: " + srcAudioFile.getName());
			
		try {
			AudioFile audioFile = AudioFileIO.read(srcAudioFile);
			Tag tag = audioFile.getTag();
			StandardArtwork stdArtwork = (StandardArtwork) tag.getFirstArtwork();
			
			//System.out.println("Cover Art Size: " + stdArtwork.getWidth() + "x" + stdArtwork.getHeight() + "px");
			
			StandardImageHandler.getInstanceOf().makeSmaller(stdArtwork, size);
			tag.setField(stdArtwork);
			audioFile.setTag(tag);
			audioFile.setTag(tag);
			AudioFileIO.write(audioFile);
			
		} catch (CannotReadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TagException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ReadOnlyFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAudioFrameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CannotWriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void resizeAll(File dir, int size) {
		final File[] audioFiles = dir.listFiles(new AudioFileFilter(false));
		
		if (audioFiles.length > 0)
        {
            for (File audioFile : audioFiles)
            {
            	resizeCoverArt(audioFile, size);
            }
        }
		
		final File[] audioFileDirs = dir.listFiles(new DirFilter());
        if (audioFileDirs.length > 0)
        {
            for (File audioFileDir : audioFileDirs)
            {
            	resizeAll(audioFileDir, size);
            }
        }
	}


	public final class DirFilter implements FileFilter
	{
	    public DirFilter()
	    {
	
	    }
	    /**
	     * Determines whether or not the file is an mp3 file.  If the file is
	     * a directory, whether or not is accepted depends upon the
	     * allowDirectories flag passed to the constructor.
	     *
	     * @param file the file to test
	     * @return true if this file or directory should be accepted
	     */
	    public final boolean accept(final File file)
	    {
	        return file.isDirectory();
	    }
	
	    public static final String IDENT = "$Id$";
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int size = 0;
		
        if (args.length == 0)
        {
            System.err.println("usage AudioImageResizer Dirname desiredSize");
            System.err.println("      You must enter the root directory and desired size");
            System.exit(1);
        }
        else if (args.length > 2)
        {
            System.err.println("usage TestAudioTagger Dirname desiredSize");
            System.err.println("      Only two parameters accepted");
            System.exit(1);
        }
        File rootDir = new File(args[0]);
        
        try {
        	size = Integer.parseInt(args[1]);
        }
        catch(NumberFormatException ex) {
        	System.err.println("usage TestAudioTagger Dirname desiredSize");
        	System.err.println("desiredSize paramater must be an Integer value");
        	System.exit(1);
        }
        
        if (!rootDir.isDirectory())
        {
            System.err.println("usage TestAudioTagger Dirname desiredSize");
            System.err.println("      Directory " + args[0] + " could not be found");
            System.exit(1);
        }
		//String mp4FileName = args[0];
		
		AudioImageResizer imageResizer = new AudioImageResizer();
				
		imageResizer.resizeAll(rootDir, size);
		
	}
}

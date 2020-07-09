package cards.main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class CardImage {
	public final static CardImage NO_CARD_IMAGE = null;
	public final static String BLANK_CARD = "cardblank";
	public final static String CARD_BACK = "cardback";
	ImageIcon image;
	String name;
	JLabel cardLabel;
	private int cardImageWidth = 75;
	private int cardImageHeight = 120;
	private int backFactor = 2;
	private int frontFactor = 1;
	
	CardImage () {
		this (BLANK_CARD);
	}
	
	CardImage (String aName) {
		setName (aName);
		loadImage ();
	}
	
	private void setCardLabel () {
		cardLabel = new JLabel ("");
//		cardLabel.setIcon (image);
		resetIconImage ();
		cardLabel.setBackground (Color.lightGray);
		if (name.equals (BLANK_CARD)) {
			System.out.println ("Blank Card Image Set");
			cardLabel.setText (BLANK_CARD);
		}
	}
	
	public void setText (String aText) {
		cardLabel.setText (aText);
	}
	
	public JLabel getCardLabel () {
		return cardLabel;
	}
	
	public void setName (String aName) {
		name = aName;
	}
	
	public String getName () {
		return name;
	}
	
	public int getCardImageWidth () {
		return cardImageWidth/getFactor ();
	}
	
	public int getCardImageHeight () {
		return cardImageHeight/getFactor ();
	}
	
	public void resetIconImage () {
		setIconImage (image);
		repaint ();
	}
	
	public void setIconImage (ImageIcon aIconImage) {
		cardLabel.setIcon (aIconImage);
	}
	
	public void repaint () {
		cardLabel.repaint ();
//		cardLabel.revalidate ();
	}
	
	public ImageIcon getImage () {
		return image;
	}
	
	public String toString () {
		return "Card Image " + getName ();
	}
	
	private int getFactor () {
		int tFactor = frontFactor;
		
		if (name.endsWith ("back")) {
			tFactor = backFactor;
		}
		
		return tFactor;
	}
	
	private void loadImage () {
		try {
			image = loadAndScaleImage ();
			setCardLabel ();
		} catch (Exception tException) {
			System.err.println ("Missing Image File " + getName ());
		}
	}

	private ImageIcon loadAndScaleImage () {
		ImageIcon tImage;
		Image tScaledImage;
		int tFactor;
		
		tFactor = getFactor ();
		tImage = new ImageIcon ("Images/" + getName () + ".jpg");
		tScaledImage = getScaledImage (tImage.getImage (), cardImageWidth/tFactor, cardImageHeight/tFactor);
		tImage.setImage (tScaledImage);
		
		return tImage;
	}
	
	private Image getScaledImage (Image srcImg, int w, int h){
	    BufferedImage resizedImg = new BufferedImage (w, h, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2 = resizedImg.createGraphics ();

	    g2.setRenderingHint (RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g2.drawImage (srcImg, 0, 0, w, h, null);
	    g2.dispose ();

	    return resizedImg;
	}
}

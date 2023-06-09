package audio;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;


/**
 * This class uses the V2 version of Google's Text to Speech API. Allows for additional specification of parameters
 * including speed and pitch.
 */
public class SynthesizerV2 extends BaseSynthesizer {

	private static final String GOOGLE_SYNTHESISER_URL
			= "https://www.google.com/speech-api/v2/synthesize?enc=mpeg&client=chromium";

	private final String API_KEY; // API_KEY used for requests

	private String languageCode;  // language of the Text you want to translate

	private double pitch = 1.0;   // The pitch of the generated audio

	private double speed = 1.0;  // The speed of the generated audio
	
	/**
	 * Constructor
	 *
	 * @param API_KEY The API-Key for Google's Speech API. An API key can be obtained by requesting
	 * one here
	 * <a href="http://www.chromium.org/developers/how-tos/api-keys">url</a>.
	 */
	public SynthesizerV2(String API_KEY){
		this.API_KEY = API_KEY;
	}
	
	/**
	 * Returns the current language code for the Synthesiser.
	 * Example: English(Generic) = en, English (US) = en-US, English (UK) = en-GB. and Spanish = es;
	 *
	 * @return the current language code
	 */
	public String getLanguage(){
		return languageCode;
	}

	/**
	 * Set language to auto to enable automatic language detection.
	 * Setting to null will also implement Google's automatic language detection
	 *
	 * @param languageCode The language code you would like to modify languageCode to.
	 */
	public void setLanguage(String languageCode){
		this.languageCode = languageCode;
	}

	/**
	 * @return the pitch
	 */
	public double getPitch() {
		return pitch;
	}

	/**
	 * Sets the pitch of the audio.
	 * Valid values range from 0 to 2 inclusive.
	 * Values above 1 correspond to higher pitch, values below 1 correspond to lower pitch.
	 *
	 * @param pitch the pitch to set
	 */
	public void setPitch(double pitch) {
		this.pitch = pitch;
	}

	/**
	 * @return the speed
	 */
	public double getSpeed() {
		return speed;
	}

	/**
	 * Sets the speed of audio.
	 * Valid values range from 0 to 2 inclusive.
	 * Values higher than one correspond to faster and vice versa.
	 *
	 * @param speed the speed to set
	 */
	public void setSpeed(double speed) {
		this.speed = speed;
	}

	/**
	 * @see BaseSynthesizer
	 */
	@Override
	public InputStream getMP3Data(String synthText) throws IOException{

		String languageCode = this.languageCode;//Ensures retention of language settings if set to auto

		if(languageCode == null || languageCode.equals("") || languageCode.equalsIgnoreCase("auto")){
			try{
				languageCode = detectLanguage(synthText);//Detects language
				if(languageCode == null){
					languageCode = "en-us";//Reverts to default language if it can't detect it.
				}
			}
			catch(Exception ex){
				ex.printStackTrace();
				languageCode = "en-us";
			}
		}

		// Changed to 4000, token limit for AI
		if(synthText.length()>4000){

			// parses String if too long
			List<String> fragments = parseString(synthText);
			String tmp = getLanguage();

			// Keeps it from autodetecting each fragment
			setLanguage(languageCode);
			InputStream out = getMP3Data(fragments);

			// Reverts to its previous language
			setLanguage(tmp);
			return out;
		}

		String encoded = URLEncoder.encode(synthText, "UTF-8");

		// Builds the URl for the API request
		StringBuilder sb = new StringBuilder(GOOGLE_SYNTHESISER_URL);
		sb.append("&key=").append(API_KEY);
		sb.append("&text=").append(encoded);
		sb.append("&lang=").append(languageCode);

		if(speed>=0 && speed<=2.0){
			sb.append("&speed=").append(speed / 2.0);
		}
		
		if(pitch>=0 && pitch<=2.0){
			sb.append("&pitch=").append(pitch / 2.0);
		}
		
		URL url = new URL(sb.toString());

		// Open new URL connection channel.
		URLConnection urlConn = url.openConnection();

		urlConn.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64;" +
				" rv:2.0) Gecko/20100101 Firefox/4.0");
		
		return urlConn.getInputStream();
	}
}

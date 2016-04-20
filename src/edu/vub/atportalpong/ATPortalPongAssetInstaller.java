package edu.vub.atportalpong;

import edu.vub.at.android.util.AssetInstaller;

public class ATPortalPongAssetInstaller extends AssetInstaller {
	// Overrides the default constructor to always copy the assets to the sdcard.
	public ATPortalPongAssetInstaller() {
      super();
      development = true;
	}
}

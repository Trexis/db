package com.db.dbs.repository;

import java.util.List;

import com.db.dbs.enums.ItemType;
import com.db.dbs.model.Preference;

public interface PreferenceRepository {
	
	List<Preference> listItemPreferences(String tenantName, String appName, ItemType itemType, String itemName);

	void updatePreference(Preference preference);
}

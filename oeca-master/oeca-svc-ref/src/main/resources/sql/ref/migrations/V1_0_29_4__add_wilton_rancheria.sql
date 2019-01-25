-- the original design does not support tribes with non-unique codes, so we're treating any negative tribal code as -999 for ICIS purposes
insert into oeca_ref_tribes(id, tribal_code, tribal_name) values(750,'-998','WILTON RANCHERIA');
insert into oeca_ref_tribe_states (tribal_code, state_code) values ('-998', 'CA');

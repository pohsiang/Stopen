package com.popo.dailyopen;

public class Def {
	
	public static String APP_NAME = "Stopen";
    public static String GA_ID = "UA-28940708-7";
	public static String START_DETAILACTIVITY_BUNDLE = "start_detail_activity_bundle";

    public static class GA{
        public static class INTERACTION{
            public static String NON_USER = "0";
            public static String USER = "1";
        }
        public static class Screen{
            public static String MAIN_ACTIVITY = "MainActivity";
            public static String GOAL_ACTIVITY = "GoalActivity";
            public static String SETTING_ACTIVITY = "SettingActivity";
        }
        public static class Action{
            public static String SCREEN_ACTION = "ScreenAction";
            public static String SET_GOAL_ACTION = "SetGoalAction";
        }
        public static class Category{
            public static String TRACE = "Trace";
        }
        public static class Label{
            public static String ON = "ON";
            public static String OFF = "OFF";
            public static String VALUE = "Value";
        }
    }


    public static class Setting{
        public static String LIMIT_HITS = "limit_hit";
        public static String USE_APP_STATUS = "use_app_status";
        public static String POP_HITS_NOTIFICATION = "pop_hits_notification";
    }

    public static class APP_STATUS{
        public static int SET_GOAL_NOT_YET = 0;
        public static int SET_GOAL_ALREADY = 1;

    }

	public static class Severe{
		public static int LEVEL1 = 30;
		public static int LEVEL2 = 60;
		public static int LEVEL3 = 90;
		public static int LEVEL4 = 120;
		public static int LEVEL5 = 150;
		public static int LEVEL6 = 180;
		public static int LEVEL7 = 210;
	}
	
	public static class DB{
		public static int VERSION = 1;
		
		public static String RECORD_TABLE = "record";
		public static String RECORD_ID = "r_id";   // Date
		public static String RECORD_OPEN_TIME = "r_open_time";
		public static String RECORD_CLOSE_TIME = "r_close_time";
	}
	
	public static class DoIntent{
        public static String INTENT_GOAL_ACTIVITY = "intent_goal_activity";
        public static String INTENT_GOAL_ACTIVITY_SHOW_CONFIRM_BUTTON = "goal_activity_show_confirm_button";
        public static String INTENT_FOR_SETTING = "intent_for_setting";
    }

	
	public static class BundleKey{
		public static String OPENDAY_OBJECT = "openday_object";
		public static String OPENDAY_DATE = "openday_date";
	}
}

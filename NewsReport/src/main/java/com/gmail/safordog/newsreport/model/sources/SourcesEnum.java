package com.gmail.safordog.newsreport.model.sources;

public enum SourcesEnum {

    TWENTYFOUR {
        public TwentyFour getSource() {return new TwentyFour();}
    },
    APOSTROPHEUA {
        public ApostropheUA getSource() {return new ApostropheUA();}
    },
    SEGODNYAUA {
        public SegodnyaUA getSource() {return new SegodnyaUA();}
    },
    STRANAUA {
        public StranaUA getSource() {return new StranaUA();}
    };
    public abstract Object getSource();
}



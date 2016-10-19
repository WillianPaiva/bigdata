public enum Alphabet {
    A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z;

    public static int getNum(String targ) {
        return valueOf(targ).ordinal();
    }

    public static int getNum(char targ) {
        return valueOf(String.valueOf(targ)).ordinal();
    }    
}

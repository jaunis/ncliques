process TCRbindFyn 3
process cCblTCRphosLCK 7
process PAGCskCD8CD45 7
process cCblTCRlig 3
process DAGPKCth 3
process Grb2SosRasGRP1 3
process FosJun 3
process ZAP70Slp76 3
process __coop0 3
process ItkRlk 3
process ZAP70Slp76PLCg_b 7
process CD45LCKTCRbind 7
process FynLCKTCRbind 7
process LCKTCRbind 3
process DAG 1
process PLCg_a 1
process Rlk 1
process CD8 1
process Slp76 1
process PKCth 1
process RasGRP1 1
process Ca 1
process Calcin 1
process CREB 1
process Gads 1
process Rsk 1
process ZAP70 1
process ERK 1
process Ras 1
process CRE 1
process TCRlig 1
process NFkB 1
process Fyn 1
process MEK 1
process cCbl 1
process IP3 1
process JNK 1
process Jun 1
process AP1 1
process LAT 1
process IkB 1
process TCRphos 1
process Fos 1
process CD45 1
process NFAT 1
process Raf 1
process PLCg_b 1
process LCK 1
process IKK 1
process PAGCsk 1
process SEK 1
process Grb2Sos 1
process TCRbind 1
process Itk 1

DAG 1 -> PKCth 0 1 @ Inf
MEK 0 -> ERK 1 0 @ Inf
LCK 0 -> cCblTCRphosLCK 1 0 @ Inf
TCRphos 1 -> cCblTCRphosLCK 1 3 @ Inf
cCbl 1 -> cCblTCRphosLCK 1 5 @ Inf
Ras 0 -> Raf 1 0 @ Inf
PKCth 0 -> SEK 1 0 @ Inf
ItkRlk 3 -> __coop0 2 3 @ Inf
ItkRlk 2 -> __coop0 2 3 @ Inf
ItkRlk 1 -> __coop0 2 3 @ Inf
ZAP70Slp76PLCg_b 6 -> __coop0 2 0 @ Inf
ZAP70Slp76PLCg_b 5 -> __coop0 2 0 @ Inf
ZAP70Slp76PLCg_b 4 -> __coop0 2 0 @ Inf
ZAP70Slp76PLCg_b 3 -> __coop0 2 0 @ Inf
ZAP70Slp76PLCg_b 2 -> __coop0 2 0 @ Inf
ZAP70Slp76PLCg_b 1 -> __coop0 2 0 @ Inf
ZAP70Slp76PLCg_b 0 -> __coop0 2 0 @ Inf
CD45 1 -> PAGCskCD8CD45 0 1 @ Inf
CD8 1 -> PAGCskCD8CD45 0 2 @ Inf
PAGCsk 1 -> PAGCskCD8CD45 0 4 @ Inf
ZAP70 0 -> LAT 1 0 @ Inf
CREB 1 -> CRE 0 1 @ Inf
TCRbind 1 -> FynLCKTCRbind 4 5 @ Inf
LCK 1 -> FynLCKTCRbind 4 6 @ Inf
Fyn 0 -> FynLCKTCRbind 4 0 @ Inf
Rlk 0 -> ItkRlk 1 0 @ Inf
Itk 1 -> ItkRlk 1 3 @ Inf
Grb2Sos 0 -> Ras 1 0 @ Inf
RasGRP1 0 -> Ras 1 0 @ Inf
LCK 0 -> Rlk 1 0 @ Inf
PLCg_b 1 -> ZAP70Slp76PLCg_b 2 3 @ Inf
Slp76 0 -> ZAP70Slp76PLCg_b 2 0 @ Inf
ZAP70 1 -> ZAP70Slp76PLCg_b 2 6 @ Inf
LAT 1 -> Gads 0 1 @ Inf
CD45 0 -> PAGCskCD8CD45 7 6 @ Inf
CD8 0 -> PAGCskCD8CD45 7 5 @ Inf
PAGCsk 0 -> PAGCskCD8CD45 7 3 @ Inf
TCRbind 1 -> LCKTCRbind 0 1 @ Inf
LCK 1 -> LCKTCRbind 0 2 @ Inf
TCRlig 1 -> cCblTCRlig 0 1 @ Inf
cCbl 1 -> cCblTCRlig 0 2 @ Inf
PLCg_b 1 -> ZAP70Slp76PLCg_b 6 7 @ Inf
Slp76 0 -> ZAP70Slp76PLCg_b 6 4 @ Inf
ZAP70 0 -> ZAP70Slp76PLCg_b 6 2 @ Inf
TCRbind 0 -> CD45LCKTCRbind 7 6 @ Inf
LCK 0 -> CD45LCKTCRbind 7 5 @ Inf
CD45 0 -> CD45LCKTCRbind 7 3 @ Inf
FosJun 3 -> AP1 0 1 @ Inf
__coop0 2 -> PLCg_a 1 0 @ Inf
__coop0 1 -> PLCg_a 1 0 @ Inf
__coop0 0 -> PLCg_a 1 0 @ Inf
Jun 0 -> FosJun 3 2 @ Inf
Fos 0 -> FosJun 3 1 @ Inf
LAT 0 -> PLCg_b 1 0 @ Inf
CD45 0 -> PAGCskCD8CD45 1 0 @ Inf
CD8 1 -> PAGCskCD8CD45 1 3 @ Inf
PAGCsk 1 -> PAGCskCD8CD45 1 5 @ Inf
TCRbind 0 -> FynLCKTCRbind 5 4 @ Inf
LCK 1 -> FynLCKTCRbind 5 7 @ Inf
Fyn 0 -> FynLCKTCRbind 5 1 @ Inf
Raf 0 -> MEK 1 0 @ Inf
PAGCsk 1 -> LCK 1 0 @ Inf
CD45 0 -> LCK 1 0 @ Inf
CD8 0 -> LCK 1 0 @ Inf
PKCth 1 -> DAGPKCth 2 3 @ Inf
DAG 0 -> DAGPKCth 2 0 @ Inf
Gads 0 -> Slp76 1 0 @ Inf
JNK 1 -> Jun 0 1 @ Inf
DAGPKCth 3 -> RasGRP1 0 1 @ Inf
TCRbind 1 -> CD45LCKTCRbind 6 7 @ Inf
LCK 0 -> CD45LCKTCRbind 6 4 @ Inf
CD45 0 -> CD45LCKTCRbind 6 2 @ Inf
IKK 0 -> IkB 0 1 @ Inf
TCRbindFyn 1 -> PAGCsk 0 1 @ Inf
TCRbindFyn 0 -> PAGCsk 0 1 @ Inf
Rsk 0 -> CREB 1 0 @ Inf
PKCth 1 -> IKK 0 1 @ Inf
TCRbind 1 -> CD45LCKTCRbind 4 5 @ Inf
LCK 1 -> CD45LCKTCRbind 4 6 @ Inf
CD45 0 -> CD45LCKTCRbind 4 0 @ Inf
PLCg_a 1 -> DAG 0 1 @ Inf
Fyn 0 -> TCRbindFyn 3 2 @ Inf
TCRbind 0 -> TCRbindFyn 3 1 @ Inf
TCRbind 0 -> CD45LCKTCRbind 5 4 @ Inf
LCK 1 -> CD45LCKTCRbind 5 7 @ Inf
CD45 0 -> CD45LCKTCRbind 5 1 @ Inf
LCKTCRbind 3 -> TCRphos 0 1 @ Inf
Fyn 1 -> TCRphos 0 1 @ Inf
ZAP70Slp76 3 -> Itk 0 1 @ Inf
CD45 1 -> PAGCskCD8CD45 2 3 @ Inf
CD8 0 -> PAGCskCD8CD45 2 0 @ Inf
PAGCsk 1 -> PAGCskCD8CD45 2 6 @ Inf
Ca 0 -> Calcin 1 0 @ Inf
TCRbind 0 -> CD45LCKTCRbind 3 2 @ Inf
LCK 0 -> CD45LCKTCRbind 3 1 @ Inf
CD45 1 -> CD45LCKTCRbind 3 7 @ Inf
TCRbind 1 -> FynLCKTCRbind 6 7 @ Inf
LCK 0 -> FynLCKTCRbind 6 4 @ Inf
Fyn 0 -> FynLCKTCRbind 6 2 @ Inf
SEK 1 -> JNK 0 1 @ Inf
CREB 0 -> CRE 1 0 @ Inf
Jun 0 -> FosJun 1 0 @ Inf
Fos 1 -> FosJun 1 3 @ Inf
Slp76 0 -> ZAP70Slp76 3 2 @ Inf
ZAP70 0 -> ZAP70Slp76 3 1 @ Inf
LAT 0 -> Grb2Sos 1 0 @ Inf
CD45LCKTCRbind 7 -> Fyn 0 1 @ Inf
CD45LCKTCRbind 6 -> Fyn 0 1 @ Inf
CD45LCKTCRbind 5 -> Fyn 0 1 @ Inf
cCbl 1 -> TCRbind 1 0 @ Inf
TCRlig 0 -> TCRbind 1 0 @ Inf
TCRbind 1 -> FynLCKTCRbind 0 1 @ Inf
LCK 1 -> FynLCKTCRbind 0 2 @ Inf
Fyn 1 -> FynLCKTCRbind 0 4 @ Inf
TCRbind 1 -> CD45LCKTCRbind 2 3 @ Inf
LCK 0 -> CD45LCKTCRbind 2 0 @ Inf
CD45 1 -> CD45LCKTCRbind 2 6 @ Inf
PLCg_b 0 -> ZAP70Slp76PLCg_b 3 2 @ Inf
Slp76 0 -> ZAP70Slp76PLCg_b 3 1 @ Inf
ZAP70 1 -> ZAP70Slp76PLCg_b 3 7 @ Inf
TCRbind 1 -> CD45LCKTCRbind 0 1 @ Inf
LCK 1 -> CD45LCKTCRbind 0 2 @ Inf
CD45 1 -> CD45LCKTCRbind 0 4 @ Inf
IkB 1 -> NFkB 1 0 @ Inf
CD45 0 -> PAGCskCD8CD45 3 2 @ Inf
CD8 0 -> PAGCskCD8CD45 3 1 @ Inf
PAGCsk 1 -> PAGCskCD8CD45 3 7 @ Inf
Fos 0 -> AP1 1 0 @ Inf
Jun 0 -> AP1 1 0 @ Inf
ItkRlk 0 -> __coop0 1 0 @ Inf
ZAP70Slp76PLCg_b 7 -> __coop0 1 3 @ Inf
ERK 1 -> Fos 0 1 @ Inf
ERK 1 -> Rsk 0 1 @ Inf
TCRbind 0 -> FynLCKTCRbind 7 6 @ Inf
LCK 0 -> FynLCKTCRbind 7 5 @ Inf
Fyn 0 -> FynLCKTCRbind 7 3 @ Inf
cCblTCRphosLCK 3 -> ZAP70 0 1 @ Inf
PLCg_b 0 -> ZAP70Slp76PLCg_b 7 6 @ Inf
Slp76 0 -> ZAP70Slp76PLCg_b 7 5 @ Inf
ZAP70 0 -> ZAP70Slp76PLCg_b 7 3 @ Inf
Rlk 1 -> ItkRlk 2 3 @ Inf
Itk 0 -> ItkRlk 2 0 @ Inf
TCRbind 0 -> CD45LCKTCRbind 1 0 @ Inf
LCK 1 -> CD45LCKTCRbind 1 3 @ Inf
CD45 1 -> CD45LCKTCRbind 1 5 @ Inf
DAG 0 -> PKCth 1 0 @ Inf
Fyn 1 -> TCRbindFyn 2 3 @ Inf
TCRbind 0 -> TCRbindFyn 2 0 @ Inf
PKCth 0 -> DAGPKCth 3 2 @ Inf
DAG 0 -> DAGPKCth 3 1 @ Inf
JNK 0 -> Jun 1 0 @ Inf
TCRbind 0 -> FynLCKTCRbind 1 0 @ Inf
LCK 1 -> FynLCKTCRbind 1 3 @ Inf
Fyn 1 -> FynLCKTCRbind 1 5 @ Inf
TCRbind 0 -> LCKTCRbind 3 2 @ Inf
LCK 0 -> LCKTCRbind 3 1 @ Inf
IKK 1 -> IkB 1 0 @ Inf
Calcin 0 -> NFAT 1 0 @ Inf
PKCth 0 -> IKK 1 0 @ Inf
PLCg_a 0 -> DAG 1 0 @ Inf
Slp76 1 -> ZAP70Slp76 2 3 @ Inf
ZAP70 0 -> ZAP70Slp76 2 0 @ Inf
PLCg_b 1 -> ZAP70Slp76PLCg_b 0 1 @ Inf
Slp76 1 -> ZAP70Slp76PLCg_b 0 2 @ Inf
ZAP70 1 -> ZAP70Slp76PLCg_b 0 4 @ Inf
TCRlig 0 -> cCblTCRlig 3 2 @ Inf
cCbl 0 -> cCblTCRlig 3 1 @ Inf
ZAP70 0 -> cCbl 1 0 @ Inf
CD45 1 -> PAGCskCD8CD45 4 5 @ Inf
CD8 1 -> PAGCskCD8CD45 4 6 @ Inf
PAGCsk 0 -> PAGCskCD8CD45 4 0 @ Inf
ZAP70 0 -> Itk 1 0 @ Inf
Slp76 0 -> Itk 1 0 @ Inf
PLCg_a 1 -> IP3 0 1 @ Inf
PLCg_b 1 -> ZAP70Slp76PLCg_b 4 5 @ Inf
Slp76 1 -> ZAP70Slp76PLCg_b 4 6 @ Inf
ZAP70 0 -> ZAP70Slp76PLCg_b 4 0 @ Inf
PKCth 1 -> DAGPKCth 0 1 @ Inf
DAG 1 -> DAGPKCth 0 2 @ Inf
RasGRP1 1 -> Grb2SosRasGRP1 2 3 @ Inf
Grb2Sos 0 -> Grb2SosRasGRP1 2 0 @ Inf
SEK 0 -> JNK 1 0 @ Inf
IP3 0 -> Ca 1 0 @ Inf
Rsk 1 -> CREB 0 1 @ Inf
TCRbindFyn 3 -> PAGCsk 1 0 @ Inf
TCRbindFyn 2 -> PAGCsk 1 0 @ Inf
Rlk 1 -> ItkRlk 0 1 @ Inf
Itk 1 -> ItkRlk 0 2 @ Inf
TCRbind 1 -> FynLCKTCRbind 2 3 @ Inf
LCK 0 -> FynLCKTCRbind 2 0 @ Inf
Fyn 1 -> FynLCKTCRbind 2 6 @ Inf
LCK 0 -> cCblTCRphosLCK 7 6 @ Inf
TCRphos 0 -> cCblTCRphosLCK 7 5 @ Inf
cCbl 0 -> cCblTCRphosLCK 7 3 @ Inf
LCKTCRbind 0 -> Fyn 1 0 @ Inf
CD45 0 -> Fyn 1 0 @ Inf
RasGRP1 0 -> Grb2SosRasGRP1 3 2 @ Inf
Grb2Sos 0 -> Grb2SosRasGRP1 3 1 @ Inf
Fyn 0 -> TCRbindFyn 1 0 @ Inf
TCRbind 1 -> TCRbindFyn 1 3 @ Inf
LAT 0 -> Gads 1 0 @ Inf
MEK 1 -> ERK 0 1 @ Inf
RasGRP1 0 -> Grb2SosRasGRP1 1 0 @ Inf
Grb2Sos 1 -> Grb2SosRasGRP1 1 3 @ Inf
Gads 1 -> Slp76 0 1 @ Inf
CD45 0 -> PAGCskCD8CD45 5 4 @ Inf
CD8 1 -> PAGCskCD8CD45 5 7 @ Inf
PAGCsk 0 -> PAGCskCD8CD45 5 1 @ Inf
Ras 1 -> Raf 0 1 @ Inf
ItkRlk 0 -> __coop0 3 2 @ Inf
ZAP70Slp76PLCg_b 6 -> __coop0 3 1 @ Inf
ZAP70Slp76PLCg_b 5 -> __coop0 3 1 @ Inf
ZAP70Slp76PLCg_b 4 -> __coop0 3 1 @ Inf
ZAP70Slp76PLCg_b 3 -> __coop0 3 1 @ Inf
ZAP70Slp76PLCg_b 2 -> __coop0 3 1 @ Inf
ZAP70Slp76PLCg_b 1 -> __coop0 3 1 @ Inf
ZAP70Slp76PLCg_b 0 -> __coop0 3 1 @ Inf
PKCth 1 -> SEK 0 1 @ Inf
TCRbind 1 -> LCKTCRbind 2 3 @ Inf
LCK 0 -> LCKTCRbind 2 0 @ Inf
LCK 1 -> cCblTCRphosLCK 6 7 @ Inf
TCRphos 0 -> cCblTCRphosLCK 6 4 @ Inf
cCbl 0 -> cCblTCRphosLCK 6 2 @ Inf
Slp76 0 -> ZAP70Slp76 1 0 @ Inf
ZAP70 1 -> ZAP70Slp76 1 3 @ Inf
ERK 0 -> Fos 1 0 @ Inf
ERK 0 -> Rsk 1 0 @ Inf
__coop0 3 -> PLCg_a 0 1 @ Inf
TCRlig 1 -> cCblTCRlig 2 3 @ Inf
cCbl 0 -> cCblTCRlig 2 0 @ Inf
Jun 1 -> FosJun 2 3 @ Inf
Fos 0 -> FosJun 2 0 @ Inf
LAT 1 -> PLCg_b 0 1 @ Inf
ZAP70 1 -> LAT 0 1 @ Inf
LCK 1 -> cCblTCRphosLCK 4 5 @ Inf
TCRphos 1 -> cCblTCRphosLCK 4 6 @ Inf
cCbl 0 -> cCblTCRphosLCK 4 0 @ Inf
Grb2SosRasGRP1 3 -> Ras 0 1 @ Inf
RasGRP1 1 -> Grb2SosRasGRP1 0 1 @ Inf
Grb2Sos 1 -> Grb2SosRasGRP1 0 2 @ Inf
LAT 1 -> Grb2Sos 0 1 @ Inf
LCK 1 -> Rlk 0 1 @ Inf
PKCth 0 -> RasGRP1 1 0 @ Inf
DAG 0 -> RasGRP1 1 0 @ Inf
cCblTCRlig 1 -> TCRbind 0 1 @ Inf
LCK 0 -> cCblTCRphosLCK 5 4 @ Inf
TCRphos 1 -> cCblTCRphosLCK 5 7 @ Inf
cCbl 0 -> cCblTCRphosLCK 5 1 @ Inf
IP3 1 -> Ca 0 1 @ Inf
IkB 0 -> NFkB 0 1 @ Inf
LCK 0 -> cCblTCRphosLCK 3 2 @ Inf
TCRphos 0 -> cCblTCRphosLCK 3 1 @ Inf
cCbl 1 -> cCblTCRphosLCK 3 7 @ Inf
FynLCKTCRbind 2 -> TCRphos 1 0 @ Inf
FynLCKTCRbind 1 -> TCRphos 1 0 @ Inf
FynLCKTCRbind 0 -> TCRphos 1 0 @ Inf
Fyn 1 -> TCRbindFyn 0 1 @ Inf
TCRbind 1 -> TCRbindFyn 0 2 @ Inf
ItkRlk 3 -> __coop0 0 1 @ Inf
ItkRlk 2 -> __coop0 0 1 @ Inf
ItkRlk 1 -> __coop0 0 1 @ Inf
ZAP70Slp76PLCg_b 7 -> __coop0 0 2 @ Inf
Rlk 0 -> ItkRlk 3 2 @ Inf
Itk 0 -> ItkRlk 3 1 @ Inf
PLCg_b 0 -> ZAP70Slp76PLCg_b 1 0 @ Inf
Slp76 1 -> ZAP70Slp76PLCg_b 1 3 @ Inf
ZAP70 1 -> ZAP70Slp76PLCg_b 1 5 @ Inf
Raf 1 -> MEK 0 1 @ Inf
PLCg_a 0 -> IP3 1 0 @ Inf
PAGCskCD8CD45 3 -> LCK 0 1 @ Inf
Ca 1 -> Calcin 0 1 @ Inf
PKCth 0 -> DAGPKCth 1 0 @ Inf
DAG 1 -> DAGPKCth 1 3 @ Inf
TCRbind 0 -> FynLCKTCRbind 3 2 @ Inf
LCK 0 -> FynLCKTCRbind 3 1 @ Inf
Fyn 1 -> FynLCKTCRbind 3 7 @ Inf
LCK 1 -> cCblTCRphosLCK 2 3 @ Inf
TCRphos 0 -> cCblTCRphosLCK 2 0 @ Inf
cCbl 1 -> cCblTCRphosLCK 2 6 @ Inf
TCRbind 0 -> LCKTCRbind 1 0 @ Inf
LCK 1 -> LCKTCRbind 1 3 @ Inf
Calcin 1 -> NFAT 0 1 @ Inf
Slp76 1 -> ZAP70Slp76 0 1 @ Inf
ZAP70 1 -> ZAP70Slp76 0 2 @ Inf
PLCg_b 0 -> ZAP70Slp76PLCg_b 5 4 @ Inf
Slp76 1 -> ZAP70Slp76PLCg_b 5 7 @ Inf
ZAP70 0 -> ZAP70Slp76PLCg_b 5 1 @ Inf
Jun 1 -> FosJun 0 1 @ Inf
Fos 1 -> FosJun 0 2 @ Inf
TCRlig 0 -> cCblTCRlig 1 0 @ Inf
cCbl 1 -> cCblTCRlig 1 3 @ Inf
LCK 1 -> cCblTCRphosLCK 0 1 @ Inf
TCRphos 1 -> cCblTCRphosLCK 0 2 @ Inf
cCbl 1 -> cCblTCRphosLCK 0 4 @ Inf
LCK 0 -> ZAP70 1 0 @ Inf
TCRphos 0 -> ZAP70 1 0 @ Inf
cCbl 1 -> ZAP70 1 0 @ Inf
CD45 1 -> PAGCskCD8CD45 6 7 @ Inf
CD8 0 -> PAGCskCD8CD45 6 4 @ Inf
PAGCsk 0 -> PAGCskCD8CD45 6 2 @ Inf
ZAP70 1 -> cCbl 0 1 @ Inf


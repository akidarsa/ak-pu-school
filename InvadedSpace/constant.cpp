#include "constant.h"

const int Db_updateDelay = 5;
const int Db_arenaWidth = 400;
const int Db_arenaHeight = 400;
const int Db_arenaMargin = 100;
const int Db_moveAreaMinX = Db_arenaMargin;
const int Db_moveAreaMaxX = Db_arenaWidth - Db_arenaMargin;


const int Db_cannonY = 350;
const int Db_cannonIndY = 10;
const int Db_cannonIndX3 = Db_arenaWidth - 20;
const int Db_cannonIndX2 = Db_arenaWidth - 40;
const int Db_cannonIndX1 = Db_arenaWidth - 60;
const int Db_bulletLength = 5;
const int Db_cannonRadius = 8;
const int Db_cannonDiameter = 2 * Db_cannonRadius;

const int Db_alienMaxNumber = 50;
const int Db_aliensPerRows = 10;
const int Db_aliensPerColumns = Db_alienMaxNumber / Db_aliensPerRows;
const int Db_alienStartX = 105;
const int Db_alienStartY = 100;
const int Db_aliensGap = 10;
const int Db_alienXSpeed = 1;
const int Db_alienYSpeed = 1;
const int Db_alienRadius = 5;
const int Db_alienDiameter = 2 * Db_alienRadius;
const int Db_beamRandSeed = 100;

const int Db_scoreX = 5;
const int Db_scoreY = 20;
const int Db_scoreInc = 10;
const int Db_hiScoreX = 5;
const int Db_hiScoreY = 30;

const QColor Db_backgroundColor = Qt::black;
const QColor Db_cannonColor3 = Qt::blue;
const QColor Db_cannonColor2 = Qt::green;
const QColor Db_cannonColor1 = Qt::red;
const QColor Db_bulletColor = Qt::white;
const QColor Db_alienColor = Qt::gray;
const QColor Db_beamColor = Qt::darkYellow;
const QColor Db_textColor = Qt::white;

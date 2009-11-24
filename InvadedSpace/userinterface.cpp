#include "userinterface.h"
#include "playarena.h"
#include "constant.h"
#include <iostream>
using namespace std;

UserInterface::UserInterface() {
    createMenuBar();
    main = new QWidget;
    QVBoxLayout * layout = new QVBoxLayout;
    arena = new PlayArena;
    main -> setLayout(layout);
    layout -> setMenuBar(bar);
    layout -> addWidget(arena);
    setCentralWidget(main);
    setWindowTitle(tr("Invaders From Space!"));

    timer = new QTimer;
    timer -> setInterval(Db_updateDelay);
    timer -> setSingleShot(false);
    connect(timer, SIGNAL(timeout()), this, SLOT(update()));

    start();
}

void UserInterface::createMenuBar() {

    bar = new QMenuBar;
    file = new QMenu(tr("&File"), this);
    reset = file->addAction(tr("&Reset"));
    clearScore = file->addAction(tr("&Clear High Score"));
    exit = file->addAction(tr("&Exit"));
    bar->addMenu(file);

    connect(reset, SIGNAL(triggered()), this, SLOT(resetarena()));
    connect(clearScore, SIGNAL(triggered()), this, SLOT(clearHiScore()));
    connect(exit, SIGNAL(triggered()), this, SLOT(exitarena()));
}

void UserInterface::clearHiScore() {
    arena->clearHiScore();
}

void UserInterface::update() {
    arena -> update();
    arena -> updateHiScore();
}

void UserInterface::start() {
    timer -> start();
}

void UserInterface::exitarena() {
    close();
}

void UserInterface::resetarena() {
    arena->resetGame();
}




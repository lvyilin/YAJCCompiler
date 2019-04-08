int main(int argc, char *argv[]) {
    struct arguments arguments;

    /* Default values. */
    arguments.cgidir = NULL;
    arguments.debugmode = 0;
    arguments.bindaddr = NULL;
    arguments.logfile = NULL;
    arguments.port = 8080;
    arguments.indexpath = NULL;

    /* Parse our arguments; every option seen by parseopt will
       be reflected in arguments. */
    argpparse(&argp, argc, argv, 0, 0, &arguments);
    char dirbuffer[1024], dirbuffer2[1024], dirbuffer3[1024];
    if (arguments.cgidir) {
        strcpy(dirbuffer, arguments.cgidir);
        arguments.cgidir = dirbuffer;
    }
    if (arguments.logfile) {
        strcpy(dirbuffer2, arguments.logfile);
        arguments.logfile = dirbuffer2;
    }
    if (arguments.indexpath) {
        strcpy(dirbuffer3, arguments.indexpath);
        arguments.indexpath = dirbuffer3;
    }
    /* Make arguments path to absolute path */
    char buffer[1024], buffer2[1024], buffer3[1024];
    toabsolutepath(arguments.indexpath, buffer3, &arguments.indexpath);
    toabsolutepath(arguments.logfile, buffer, &arguments.logfile);
    toabsolutepath(arguments.cgidir, buffer2, &arguments.cgidir);
    printf("%s\n%s\n%s\n", arguments.cgidir, arguments.logfile, arguments.indexpath);
    FILE *logger = NULL;
    if (arguments.logfile != NULL) {
        logger = makelogger(arguments.logfile);
    } else {
        logger = stdout;
    }

    startlistener(arguments.port, arguments.bindaddr, logger, arguments.indexpath, arguments.cgidir,
                   arguments.debugmode);

    closelogger(logger);
//    exit(0);
}
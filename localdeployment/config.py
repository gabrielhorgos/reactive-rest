#!/usr/bin/python

import os, sys
from optparse import OptionParser
import logging.config
from subprocess import Popen
import shutil

# absolute path of local directory containing this script
local_abs_path = os.path.dirname(os.path.abspath(__file__))

# initialize logging
logging.config.fileConfig(os.path.join(local_abs_path, 'log.conf'))
_LOG = logging.getLogger()
# shortcuts
lgi, lgw, lge, lgex, dbg = _LOG.info, _LOG.warn, _LOG.error, _LOG.exception, _LOG.debug

# globals
version = "Configure PayaraMicro 5 instance for running the reactive-rest (LOCAL and CI environments)"
options = None

# GCDM_HOME
if os.getenv('GCDM_HOME'):
	GCDM_HOME = os.getenv('GCDM_HOME')
	lgi('GCDM_HOME = %s' % (GCDM_HOME));
else:
	GCDM_HOME = os.path.join('c:\work\gcdm')
	lgi('Variable GCDM_HOME is not set, using default value %s' % (GCDM_HOME));

# exit codes
class ExitCode:
    OK = 0
    ERROR = 1

# illegal argument exception
class InvalidCmdArguments(Exception): pass
# service error exception
class ProcessError(Exception): pass

def run_command(cmdlist):
    dbg("running: " + " ".join(cmdlist))
    if options.simulate: return

    p = Popen(cmdlist)
    p.wait()
    if p.returncode != 0:
        raise ProcessError("process failed with code %i" % p.returncode)

# asadmin commands running without admin user
def runCommand(command, *params):
    run_command([os.path.join(options.jvmhome, 'java.exe'), '-jar', os.path.join(options.appserver, 'payara-micro-5.182.jar'), command] + list(params))

def main():
    try:
        print(version)

        # define command line options
        parser = OptionParser(usage="%prog [options]", version="%prog - " + version)
        parser.add_option("--simulate", help="do not call asadmin", action="store_true", default=False)
        parser.add_option("--portbase", help="see asadmin --portbase option", type="int", default=6000)
        parser.add_option("--domain", help="Glassfish domain name", default='gcdm-login')
        parser.add_option("--appserver", help="Payara Micro root path", default= os.path.join(GCDM_HOME, 'bin\PayaraMicro'))
        parser.add_option("--jvmhome", help="JAVA HOME", default= os.path.join(GCDM_HOME, 'bin\\jdk\\jdk1.8\\bin'))
        parser.add_option("--deploy", help="do deployment only", action="store_true", default=False)
        parser.add_option("--restart", help="restart the domain", action="store_true", default=False)
        parser.add_option("--stop", help="stop the domain", action="store_true", default=False)
        parser.add_option("--start", help="start the domain", action="store_true", default=False)
        parser.add_option("--delete", help="delete the domain", action="store_true", default=False)
        parser.add_option("--create", help="create the domain", action="store_true", default=False)
        parser.add_option("--nodebug", help="do not use --debug when starting domain", action="store_true", default=False)
        parser.add_option("--mqpath", help="Web Sphere MQ root path")

        # parse command line
        global options
        (options, args) = parser.parse_args()
        if args: raise InvalidCmdArguments("unknown arguments: %s" % args)
        if not (options.deploy or options.restart or options.stop or options.start or options.delete or options.create):
            parser.error("one of options is required: --start")
        if options.nodebug:
            options.debug = '--debug=false'
        else:
            options.debug = '--debug=true'
        dbg("command line parsed: " + str(options))

        if options.start:
            warPath = os.path.join(os.path.dirname(local_abs_path), 'target', 'registration*' + '.war')
            runCommand('--deploy', warPath)

        lgi("success")

        return ExitCode.OK

    except SystemExit:
        # ignore parser thrown exception
        pass

    except Exception, e:
        lgex(str(e))
        return ExitCode.ERROR

if __name__ == '__main__':
    sys.exit(main())

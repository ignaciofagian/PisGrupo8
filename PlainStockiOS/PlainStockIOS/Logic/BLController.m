#import "BLController.h"
#import <RestKit/RestKit.h>
#import "WSManager.h"
#import "WSResponse.h"
#import "DBManager.h"

#define CASH_UPDATE_INTERVAL_DEFAULT 120 //seconds

@implementation BLController{
    NSTimer *timerCashHistory;
    double cashUpdateInterval;
}

static BLController *instance = nil;


+ (BLController *)sharedInstance
{
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        instance = [[self alloc] init];
    });
    return instance;
}

- (id)init
{
    if (self = [super init])
    {
        //register for 'going background event' for stoping updater tasks
        [[NSNotificationCenter defaultCenter] addObserver:self
                                                 selector:@selector(stopCashHistoryUpdater)
                                                     name:UIApplicationWillResignActiveNotification
                                                   object:nil];
        
        //register for 'coming from background event' for stopping updater tasks
        [[NSNotificationCenter defaultCenter] addObserver:self
                                                 selector:@selector(startCashHistoryUpdaterIfNeeded)
                                                     name:UIApplicationDidBecomeActiveNotification
                                                   object:nil];
        
        NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
        
        //language
        NSString *language = [defaults objectForKey:@"language"];
        if (!language)
        {
            language = @"ENG";
            [defaults setObject:language forKey:@"language"];
            [defaults synchronize];
        }
        else
            _language = [language isEqualToString:@"SPA"] ? SPA : ENG;
        
        //userID
        _userId = [defaults objectForKey:@"userId"];
        NSLog(@"%@", self.userId);
        
        //time machine date
        _isOnTimeMachineMode = [defaults boolForKey:@"isOnTimeMachineMode"];
        
        if (_isOnTimeMachineMode)
            _timeMachineDate = [defaults objectForKey:@"timeMachineDate"];
        
        //last persisted date
        _lastDatePersisted = [[DBManager sharedInstance] getLastPersistedDate];
        
        //update interval
        NSDictionary *dicConfig = [NSDictionary dictionaryWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"config" ofType:@"plist"]];
        cashUpdateInterval = [dicConfig objectForKey:@"CASH_UPDATE_INTERVAL"] != nil ? [[dicConfig objectForKey:@"CASH_UPDATE_INTERVAL"] doubleValue] : CASH_UPDATE_INTERVAL_DEFAULT;
    }
    return self;
}

-(void)setLanguage:(Language)language
{
    _language = language;
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    
    if (language == ENG)
        [defaults setObject:@"ENG" forKey:@"language"];
    else
        [defaults setObject:@"SPA" forKey:@"language"];
}

-(void)setUserId:(NSString *)userId
{
    _userId = userId;
    //NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    //[defaults setObject:userId forKey:@"userId"];
    //[defaults synchronize];
}

-(void)setIsOnTimeMachineMode:(BOOL)isOnTimeMachineMode
{
    _isOnTimeMachineMode = isOnTimeMachineMode;
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    [defaults setBool:isOnTimeMachineMode forKey:@"isOnTimeMachineMode"];
    [defaults synchronize];
    [self startCashHistoryUpdaterIfNeeded];
}

-(void)setTimeMachineDate:(NSDate *)timeMachineDate
{
    _timeMachineDate = timeMachineDate;
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    [defaults setObject:timeMachineDate forKey:@"timeMachineDate"];
    [defaults synchronize];
}

- (void)startCashHistoryUpdaterIfNeeded
{
    if (self.userId != nil && timerCashHistory == nil && !self.isOnTimeMachineMode)
    {
        NSLog(@"Started date cash history updater timer.");
        timerCashHistory = [NSTimer scheduledTimerWithTimeInterval:cashUpdateInterval target:self selector:@selector(getCashHistoryTask) userInfo:nil repeats:YES];
        [timerCashHistory fire];
    }
}

- (void)stopCashHistoryUpdater
{
    if (timerCashHistory != nil)
    {
        [timerCashHistory invalidate];
        timerCashHistory = nil;
    }
}

- (void)getCashHistoryTask
{
    NSLog(@"Get Cash History Task");
    
    if (self.userId != nil && !self.isOnTimeMachineMode && !([BLController sharedInstance].lastDatePersisted == DBL_MIN))
    {
        double dateFrom = self.lastDatePersisted;
        
        if (dateFrom != 0)
            dateFrom += 60000; //+1 min for not getting last date_cash again
        
        NSString *stringDateFrom = [[NSNumber numberWithDouble:dateFrom] stringValue];
        
        [[WSManager sharedInstance] getCashHistoryFrom:stringDateFrom to:@"0"];
    }
}


- (void) showConnectionErrorAlert
{
    Language lang = [BLController sharedInstance].language;
    NSString *title = @"Error";
    NSString *ok = (lang == ENG) ? @"OK" : @"Aceptar";
    NSString *msg = (lang == ENG) ? @"Couldn't connect to the server, please try again later." : @"No fue posible comunicarse con el servidor, vuelva a intentar luego.";
    
    UIAlertView * alert =[[UIAlertView alloc ] initWithTitle:title
                                                     message:msg
                                                    delegate:self
                                           cancelButtonTitle:ok
                                           otherButtonTitles: nil];
    [alert show];
}

- (void) showDBErrorAlert
{
    Language lang = [BLController sharedInstance].language;
    NSString *title = @"Error";
    NSString *ok = (lang == ENG) ? @"OK" : @"Aceptar";
    NSString *msg = (lang == ENG) ? @"Persistance error occurred. Check if device is out of memory and try again." : @"Ha ocurrido un error de persistencia. Revisa que el dispositivo tenga memoria libre y vuelve a intentarlo.";
    
    UIAlertView * alert =[[UIAlertView alloc ] initWithTitle:title
                                                     message:msg
                                                    delegate:self
                                           cancelButtonTitle:ok
                                           otherButtonTitles: nil];
    [alert show];
}

@end
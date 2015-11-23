#import "DBManager.h"
#import "DataDateCash.h"
#import "BLController.h"
#import "WSManager.h"

NSString *const DB_NAME = @"plainstock.db";

static sqlite3 *database = nil;
static sqlite3_stmt *statement = nil;


@implementation DBManager

static DBManager *instance = nil;

+ (DBManager *)sharedInstance
{
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        instance = [[super allocWithZone:nil]init];
        [instance createDB];
    });
    return instance;
}

//Creates a new DB if it doesn't already exist
-(BOOL)createDB{
    
    BOOL success = NO;
    
    @try {
        NSString *docsDir;
        NSArray *dirPaths;
        
        // Get the documents directory
        dirPaths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
        docsDir = dirPaths[0];
        
        // Build the path to the database file
        NSDictionary *dicConfig = [NSDictionary dictionaryWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"config" ofType:@"plist"]];
        NSString *dbName = [dicConfig objectForKey:@"DB_NAME"] != nil ? [dicConfig objectForKey:@"DB_NAME"] : DB_NAME;
        self.databasePath = [[NSString alloc] initWithString:[docsDir stringByAppendingPathComponent: dbName]];
        
        NSFileManager *filemgr = [NSFileManager defaultManager];
        if ([filemgr fileExistsAtPath: self.databasePath ] == NO)
        {
            const char *dbpath = [self.databasePath UTF8String];
            if (sqlite3_open(dbpath, &database) == SQLITE_OK)
            {
                char *errMsg;
                const char *sql_stmt_balance = "create table if not exists saldo_table (id_saldo INTEGER PRIMARY KEY AUTOINCREMENT, date_saldo REAL NOT NULL, saldo_saldo REAL NOT NULL)";
                success = (sqlite3_exec(database, sql_stmt_balance, NULL, NULL, &errMsg) == SQLITE_OK);
                
                //const char *sql_stmt_userInfo = "create table if not exists userInfo(userInfoID INTEGER PRIMARY KEY AUTOINCREMENT, userID text, language text, age integer);";
                
                if (!success)
                    NSLog(@"DB_MSG: Failed to create table 'saldo_table'");
            }
            else {
                NSLog(@"DB_MSG: Failed to open/create database");
            }
        }
    }
    @catch (NSException *e) {
        success = NO;
        NSLog(@"DB_Exception - createDB failed with error: %@", e);
    }
    @finally {
        sqlite3_close(database);
    }
    
    return success;
}

//Deletes the DB from filesystem
-(BOOL)deleteDB
{
    BOOL success = NO;
    @try {
        
        if([[NSFileManager defaultManager] fileExistsAtPath:self.databasePath])
        {
            [[NSFileManager defaultManager] removeItemAtPath:self.databasePath error:nil];
            success = YES;
        }
    }
    @catch (NSException *e) {
        success = NO;
        NSLog(@"DB_Exception - deleteDB failed with error: %@", e);
    }
    @finally {
        //do nothing
    }
    
    return success;
}


-(BOOL)resetDB
{
    BOOL success = NO;
    
    @try {
        const char *dbpath = [self.databasePath UTF8String];
        if (sqlite3_open(dbpath, &database) == SQLITE_OK)
        {
            NSString *deleteSQL = @"delete from saldo_table";
            const char *delete_stmt = [deleteSQL UTF8String];
            char *errMsg;
            success = sqlite3_exec(database, delete_stmt, NULL, NULL, &errMsg) == SQLITE_OK;
            
            if (success)
                NSLog(@"DB_MSG: saldo_table has been truncated.");
            else
                NSLog(@"DB_MSG: Failed to truncate saldo_table.");
        }
        else
        {
            NSLog(@"DB_ERROR: Failed to truncate saldo_table. Could not open DB.");
        }
    }
    @catch (NSException *e) {
        success = NO;
        NSLog(@"DB_Exception - resetDB failed with error: %@", e);
    }
    @finally {
        sqlite3_close(database);
    }
    
    return success;
    
}


//Calls saveDate for all dataCash on the 'data' array
- (BOOL) saveDateCashPeriod:(NSArray *)data
{
    BOOL success = NO;
    
    @try {
        const char *dbpath = [self.databasePath UTF8String];
        int result = sqlite3_open(dbpath, &database);
        
        if (result == SQLITE_OK)
        {
            
            NSDateFormatter *dateFormatter = [self getDefaultDateFormatter];
            
            double dateEpoch, cash;
            NSDate *date = [[NSDate alloc] init];
            
            NSMutableString* insertSQLMutable = [NSMutableString stringWithString:@"INSERT INTO saldo_table (date_saldo, saldo_saldo) VALUES (?,?)"];
            
            for (int i = 0; i < data.count - 1; i++)
                [insertSQLMutable appendString:@",(?,?)"];
            [insertSQLMutable appendString:@";"];
            
            NSString *insertSQL = [insertSQLMutable copy];
            const char *insert_stmt = [insertSQL UTF8String];
            sqlite3_prepare_v2(database, insert_stmt,-1, &statement, NULL);
            
            DataDateCash *balance;
            
            for (int i = 0; i < data.count; i++)
            {
                balance = [data objectAtIndex:i];
                date = [dateFormatter dateFromString:balance.date];
                dateEpoch = [date timeIntervalSince1970]*1000; //epoch
                cash = [balance.cash doubleValue];
                sqlite3_bind_double(statement, i*2+1, dateEpoch);
                sqlite3_bind_double(statement, i*2+2, cash);
            }
            
            int res = sqlite3_step(statement);
            success = (res == SQLITE_DONE);
            
            if (success)
            {
                balance = [data lastObject];
                [BLController sharedInstance].lastDatePersisted = [[dateFormatter dateFromString:balance.date] timeIntervalSince1970]*1000;
            }
            else
            {
                NSLog(@"Database Error Message : %s", sqlite3_errmsg(database));
                NSLog(@"DB_MSG: Failed to save date_cash array to DB. Failed to insert into saldo_table. Error code: %d", res);
            }
        }
        else
        {
            NSLog(@"DB_ERROR: Failed to save date_cash array to DB. Could not open DB. Error code: %d", result);
        }
    }
    @catch (NSException *e) {
        success = NO;
        NSLog(@"DB_Exception - Failed to save date_cash. Error: %@", e);
    }
    @finally {
        sqlite3_close(database);
    }

    return success;
}


//Get rows from table saldos_table, filter by date 'from' 'to'
//'from' and 'to' represents the dates with format "yyyy-MM-dd HH:mm"
-(NSMutableArray *)getCashHistoryFrom:(double)iniDate to:(double)endDate
{
    NSMutableArray *dateCashInfo = [[NSMutableArray alloc] init];
        
    @try {
        
        NSDateFormatter *dateFormatter = [self getDefaultDateFormatter];
        
        // Setup the database object
        sqlite3 *database;
        
        if(sqlite3_open([self.databasePath UTF8String], &database) == SQLITE_OK)
        {
            NSString *sqlStatement_userInfo =[NSString stringWithFormat: @"select * from saldo_table where date_saldo >= ? and date_saldo <= ? order by date_saldo asc"];
            sqlite3_stmt *compiledStatement;
            
            if(sqlite3_prepare_v2(database, [sqlStatement_userInfo UTF8String], -1, &compiledStatement, NULL) == SQLITE_OK)
            {
                sqlite3_bind_double(compiledStatement, 1, iniDate);
                sqlite3_bind_double(compiledStatement, 2, endDate);
                // Loop through the results and add them to the feeds array
                while(sqlite3_step(compiledStatement) == SQLITE_ROW)
                {
                    /*get ID, not used, debugging purposes*/
                    sqlite3_column_int(compiledStatement, 0);
                    
                    /*get Date*/
                    double doubleDate = sqlite3_column_double(compiledStatement, 1);
                    NSDate *resultDate = [NSDate dateWithTimeIntervalSince1970:doubleDate/1000];
                    NSString *stringDate = [dateFormatter stringFromDate:resultDate];
                    
                    /*get Cash*/
                    double cash = sqlite3_column_double(compiledStatement, 2);
                    NSNumber *doubleCash = [NSNumber numberWithDouble:cash];
                    NSString* cashString = [doubleCash stringValue];
                    
                    //Add date-cash info to returning result
                    DataDateCash *infoDateCash = [[DataDateCash alloc] initWithDate:stringDate cash:cashString lost:NO];
                    [dateCashInfo addObject:infoDateCash];
                }
            }
            else
            {
                NSLog(@"DB_MSG: Failed to get cash history. Failed to prepare statement.");
            }
            
            // Release the compiled statement from memory
            sqlite3_finalize(compiledStatement);
        }
        else
        {
            NSLog(@"DB_ERROR: Failed to get cash history. Could not open DB.");
        }
        
    }
    @catch (NSException *e) {
        NSLog(@"DB_Exception: Failed to get cash history with error: %@", e);
        [dateCashInfo removeAllObjects];
    }
    @finally {
        sqlite3_close(database);
    }
    
    return dateCashInfo;
}


//Returns last date_cash persisted date in epoch (miliseconds).
//Returns 0 if there are no records
//Returns DBL_MIN on error
-(double)getLastPersistedDate
{
    double lastDatePersistedEpoch = 0;
    NSMutableArray *dateCashInfo = [[NSMutableArray alloc] init];

    @try {
        NSDateFormatter *dateFormatter = [self getDefaultDateFormatter];
        
        // Setup the database object
        sqlite3 *database;

        if(sqlite3_open([self.databasePath UTF8String], &database) == SQLITE_OK)
        {
            NSString *sqlStatement_userInfo =[NSString stringWithFormat:
                                              @"select * from saldo_table where date_saldo = (select max(date_saldo) from saldo_table)"];
            sqlite3_stmt *compiledStatement;
     
            if(sqlite3_prepare_v2(database, [sqlStatement_userInfo UTF8String], -1, &compiledStatement, NULL) == SQLITE_OK)
            {
                // Loop through the results and add them to the array
                int results = 0;
                while(sqlite3_step(compiledStatement) == SQLITE_ROW)
                {
                    results ++;
                    
                    /*get ID, not used, debugging purposes*/
                    //int id = sqlite3_column_int(compiledStatement, 0);
                    
                    /*get Date*/
                    double doubleDate = sqlite3_column_double(compiledStatement, 1);
                    NSDate *resultDate = [NSDate dateWithTimeIntervalSince1970:doubleDate/1000];
                    NSString *stringDate = [dateFormatter stringFromDate:resultDate];
                    
                    /*get Cash*/
                    double cash = sqlite3_column_double(compiledStatement, 2);
                    NSNumber *doubleCash = [NSNumber numberWithDouble:cash];
                    NSString* cashString = [doubleCash stringValue];
                    
                    DataDateCash *infoDateCash = [[DataDateCash alloc] initWithDate:stringDate cash:cashString lost:NO];
                    [dateCashInfo addObject:infoDateCash];
                }
                
                if (results > 0)
                {
                    DataDateCash *lastDateCash = [dateCashInfo objectAtIndex:0];
                    lastDatePersistedEpoch = [[dateFormatter dateFromString:lastDateCash.date] timeIntervalSince1970]*1000;
                }
            }
            else
            {
                NSLog(@"DB_MSG: Failed to get las persisted date. Failed to prepare statement.");
                lastDatePersistedEpoch = DBL_MIN;
            }
            
            // Release the compiled statement from memory
            sqlite3_finalize(compiledStatement);
        }
        else
        {
            NSLog(@"DB_ERROR: Failed to get last persisted date. Could not open DB.");
            lastDatePersistedEpoch = DBL_MIN;
        }
    }
    @catch (NSException *e) {
        NSLog(@"DB_Exception - getLastPersistedDate failed with error: %@", e);
        lastDatePersistedEpoch = DBL_MIN;
    }
    @finally {
        sqlite3_close(database);
    }
    
    return lastDatePersistedEpoch;
}


-(NSDateFormatter *)getDefaultDateFormatter
{
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:@"yyyy-MM-dd HH:mm"];
    [dateFormatter setTimeZone:[NSTimeZone timeZoneWithName:@"GMT"]];
    return dateFormatter;
}


@end
#import "DBManager.h"
#import "DataDateCash.h"

static DBManager *sharedInstance = nil;
static sqlite3 *database = nil;
static sqlite3_stmt *statement = nil;

@implementation DBManager

+(DBManager*)getSharedInstance{
    if (!sharedInstance) {
        sharedInstance = [[super allocWithZone:NULL]init];
        [sharedInstance createDB];
    }
    return sharedInstance;
}

-(BOOL)createDB{
    NSString *docsDir;
    NSArray *dirPaths;
    // Get the documents directory
    dirPaths = NSSearchPathForDirectoriesInDomains
    (NSDocumentDirectory, NSUserDomainMask, YES);
    docsDir = dirPaths[0];
    // Build the path to the database file
    databasePath = [[NSString alloc] initWithString:
                    [docsDir stringByAppendingPathComponent: @"mauri.db"]];
    BOOL isSuccess = YES;
    NSFileManager *filemgr = [NSFileManager defaultManager];
    if ([filemgr fileExistsAtPath: databasePath ] == NO)
    {
        const char *dbpath = [databasePath UTF8String];
        if (sqlite3_open(dbpath, &database) == SQLITE_OK)
        {
            char *errMsg;
             const char *sql_stmt =
            "create table if not exists saldo_table (id_saldo INTEGER PRIMARY KEY AUTOINCREMENT, \
            date_saldo REAL NOT NULL, saldo_saldo REAL NOT NULL)";
            if (sqlite3_exec(database, sql_stmt, NULL, NULL, &errMsg)
                != SQLITE_OK)
            {
                isSuccess = NO;
                NSLog(@"Failed to create table");
            }
            sqlite3_close(database);
            return  isSuccess;
        }
        else {
            isSuccess = NO;
            NSLog(@"Failed to open/create database");
        }
    }
    return isSuccess;
}


- (BOOL) saveDate:(NSString*)date Saldo:(NSString*)saldo
{
    //TODO MAURI: REVISAR COMO ES EL TEMA DE LAS FECHAS, DEFINIR BIEN COMO ES EL FORMATO DE LA FECHA
    //TODO MAURI: PARA TODO EL SISTEMA, INCLUSO PARA LA BASE DE DATOS AUNQUE ESTOY GUARDANDO UN DOUBLE, PERO AFECTA EN LA CONVERSION
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:@"yyyy-MM-dd HH:mm"];
    [dateFormatter setTimeZone:[NSTimeZone timeZoneWithName:@"GMT"]];
    NSDate *dateFromString = [[NSDate alloc] init];
    
    dateFromString = [dateFormatter dateFromString:date];
    
    const char *dbpath = [databasePath UTF8String];
    if (sqlite3_open(dbpath, &database) == SQLITE_OK)
    {
        NSString *comienzoFecha = @"1900-01-01 00:00";
        NSDate *comienzo = [[NSDate alloc]init];
        comienzo = [dateFormatter dateFromString:comienzoFecha];
        
        double fecha = [dateFromString timeIntervalSinceDate:comienzo];
        double cash = [saldo doubleValue];
        
        NSString *insertSQL = @"insert into saldo_table (date_saldo, saldo_saldo) values (?,?)";
        const char *insert_stmt = [insertSQL UTF8String];
        
        sqlite3_prepare_v2(database, insert_stmt,-1, &statement, NULL);
        sqlite3_bind_double(statement, 1, fecha);
        sqlite3_bind_double(statement, 2, cash);
        
        if (sqlite3_step(statement) == SQLITE_DONE)
        {
            return YES;
        }
        else {
            return NO;
        }
        sqlite3_reset(statement);
    }
    return NO;
}


- (BOOL) saveDateCashPeriod:(NSArray *)datos
{
    bool success= YES;
    int length = [datos count];
    for (int indexValue = 0; indexValue< length; indexValue++)
    {
        DataDateCash *balance = [datos objectAtIndex:indexValue];
        NSString *fecha = balance.date;
        NSString *saldo = balance.cash;
        success = [self saveDate:fecha Saldo:saldo];
        if (success == NO){       //TODO MAURI: ESTUDIAR BIEN QUE HACEMOS ACA, QUE PASA SI PERSISITO 3 ROWS Y LA 4 DIO ERROR POR EJ
            return success;
        }
        
    }
    return success;
}


-(NSMutableArray *)readInformationFromDatabase
{
    NSMutableArray *array = [[NSMutableArray alloc] init];
    
    // Setup the database object
    sqlite3 *database;
    
    // Open the database from the users filessytem
    if(sqlite3_open([databasePath UTF8String], &database) == SQLITE_OK)
    {
        // Setup the SQL Statement and compile it for faster access
        //SQLIte Statement
        NSString *sqlStatement_userInfo =[NSString stringWithFormat:@"Select * from saldo_table order by date_saldo desc"];
        
        sqlite3_stmt *compiledStatement;
        
        
        if(sqlite3_prepare_v2(database, [sqlStatement_userInfo UTF8String], -1, &compiledStatement, NULL) == SQLITE_OK)
        {
            
            // Loop through the results and add them to the feeds array
            while(sqlite3_step(compiledStatement) == SQLITE_ROW)
            {
                // Init the Data Dictionary
                NSMutableDictionary *_dataDictionary=[[NSMutableDictionary alloc] init];
                
                /*OBTENGO EL ID*/
                int id = sqlite3_column_int(compiledStatement, 0);
                /**************/
                
                /*OBTENGO FECHA SALDO*/
                double fecha = sqlite3_column_double(compiledStatement, 1);
                
                NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
                [dateFormatter setDateFormat:@"yyyy-MM-dd HH:mm"];
                [dateFormatter setTimeZone:[NSTimeZone timeZoneWithName:@"GMT"]];
                NSString *comienzoFecha = @"1900-01-01 00:00";
                NSDate *comienzo = [[NSDate alloc]init];
                comienzo = [dateFormatter dateFromString:comienzoFecha];
                NSDate *resultado = [NSDate dateWithTimeInterval:fecha sinceDate:comienzo];
                
               
                NSString *stringDate = [dateFormatter stringFromDate:resultado];
               
                /*********************/
                
                /*OBTENGO EL SALDO*/
                double saldo = sqlite3_column_double(compiledStatement, 2);
                NSNumber *myDoubleNumber = [NSNumber numberWithDouble:saldo];
                
                NSString* cashString = [myDoubleNumber stringValue];
                /*****************/
                DataDateCash *infoDateCash = [[DataDateCash alloc] initWithDate:stringDate cash:cashString];

                [array addObject:infoDateCash];
            }
        }
        else
        {
            NSLog(@"No Data Found");
        }
        
        // Release the compiled statement from memory
        sqlite3_finalize(compiledStatement);
    }
    
    sqlite3_close(database);
    
    return array;
}

-(NSMutableArray *)readInformationFrom:(NSString *)desde To:(NSString *)hasta
{
    
    NSDateFormatter *dateFormat = [[NSDateFormatter alloc] init];
    [dateFormat setDateFormat:@"yyyy-MM-dd HH:mm"];
    [dateFormat setTimeZone:[NSTimeZone timeZoneWithName:@"GMT"]];
    
    NSDate *dateFrom = [[NSDate alloc] init];
    dateFrom = [dateFormat dateFromString:desde];
    
    NSDate *dateTo = [[NSDate alloc] init];
    dateTo = [dateFormat dateFromString:hasta];
    
    
    NSString *comienzoFecha = @"1900-01-01 00:00";
    NSDate *comienzo = [[NSDate alloc]init];
    comienzo = [dateFormat dateFromString:comienzoFecha];
    
    double fechaDesde = [dateFrom timeIntervalSinceDate:comienzo];
    double fechaHasta = [dateTo timeIntervalSinceDate:comienzo];
    
    NSMutableArray *array = [[NSMutableArray alloc] init];
    
    // Setup the database object
    sqlite3 *database;
    

    if(sqlite3_open([databasePath UTF8String], &database) == SQLITE_OK)
    {
        NSString *sqlStatement_userInfo =[NSString stringWithFormat:
                                          @"Select * from saldo_table where date_saldo >= ? and date_saldo <= ? order by date_saldo asc"];
        sqlite3_stmt *compiledStatement;
        
        
        if(sqlite3_prepare_v2(database, [sqlStatement_userInfo UTF8String], -1, &compiledStatement, NULL) == SQLITE_OK)
        {
            sqlite3_bind_double(compiledStatement, 1, fechaDesde);
            sqlite3_bind_double(compiledStatement, 2, fechaHasta);
            // Loop through the results and add them to the feeds array
            while(sqlite3_step(compiledStatement) == SQLITE_ROW)
            {
               
                NSMutableDictionary *_dataDictionary=[[NSMutableDictionary alloc] init];
           
                int id = sqlite3_column_int(compiledStatement, 0);
                /**************/
               
                double fecha = sqlite3_column_double(compiledStatement, 1);
                
                NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
                [dateFormatter setDateFormat:@"yyyy-MM-dd HH:mm"];
                [dateFormatter setTimeZone:[NSTimeZone timeZoneWithName:@"GMT"]];
                NSString *comienzoFecha = @"1900-01-01 00:00";
                NSDate *comienzo = [[NSDate alloc]init];
                comienzo = [dateFormatter dateFromString:comienzoFecha];
                NSDate *resultado = [NSDate dateWithTimeInterval:fecha sinceDate:comienzo];
                
                
                NSString *stringDate = [dateFormatter stringFromDate:resultado];
                
        
                double saldo = sqlite3_column_double(compiledStatement, 2);
                NSNumber *myDoubleNumber = [NSNumber numberWithDouble:saldo];
                
                NSString* cashString = [myDoubleNumber stringValue];
                
                DataDateCash *infoDateCash = [[DataDateCash alloc] initWithDate:stringDate cash:cashString];
                
                [array addObject:infoDateCash];
            }
        }
        else
        {
            NSLog(@"No Data Found");
        }
        
        // Release the compiled statement from memory
        sqlite3_finalize(compiledStatement);
    }
    
    sqlite3_close(database);
    
    return array;
}








@end
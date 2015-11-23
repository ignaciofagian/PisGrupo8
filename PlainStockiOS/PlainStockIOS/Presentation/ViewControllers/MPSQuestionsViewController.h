//
//  MPSQuestionsViewController.h
//  PlainStockiOS
//
//  Created by Bruno Rodao on 9/29/15.
//  Copyright (c) 2015 FING. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MPSNavBarMenuOptionButton.h"
#import "MPSAnswerButton.h"
#import "BLController.h"

@interface MPSQuestionsViewController : UIViewController <BLControllerDelegate>

@property (weak, nonatomic) IBOutlet UIBarButtonItem *navBarMenuButton;
@property (weak, nonatomic) IBOutlet MPSNavBarMenuOptionButton *navBarMenuOptionButton;
//TODO: todos estos botones se van, tambien del storyboard, hay que agregar botones programaticamente y setear
//      las constraints para posicionarlos en pantalla de forma dinamica, luego de obtener la cantidad de respuestas
@property (weak, nonatomic) IBOutlet MPSAnswerButton *yesButton;
@property (weak, nonatomic) IBOutlet MPSAnswerButton *maybeButton;
@property (weak, nonatomic) IBOutlet MPSAnswerButton *noButton;
@property (weak, nonatomic) IBOutlet UIButton *previousQuestionButton;
@property (weak, nonatomic) IBOutlet UIButton *nextQuestionButton;
@property (weak, nonatomic) IBOutlet UIButton *confirmAnswersButton;
@property (weak, nonatomic) IBOutlet UILabel *confirmAnswersLabel;
@property (weak, nonatomic) IBOutlet UILabel *questionLabel;


- (IBAction)navBarMenuButtonPressed:(id)sender;
- (IBAction)resetQuestionsButtonPressed:(id)sender;
//TODO: estas acciones se deben borrar una ves se borren los botones de arriba
- (IBAction)yesButtonPressed:(id)sender;
- (IBAction)maybeButtonPressed:(id)sender;
- (IBAction)noButtonPressed:(id)sender;
- (IBAction)nextQuestionButtonPressed:(id)sender;
- (IBAction)previousQuestionButtonPressed:(id)sender;
- (IBAction)confirmAnswersButtonPressed:(id)sender;


@end

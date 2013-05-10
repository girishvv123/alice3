/*
* Alice 3 End User License Agreement
 * 
 * Copyright (c) 2006-2013, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice", nor may "Alice" appear in their name, without prior written permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software must display the following acknowledgement: "This product includes software developed by Carnegie Mellon University"
 * 
 * 5. The gallery of art assets and animations provided with this software is contributed by Electronic Arts Inc. and may be used for personal, non-commercial, and academic use only. Redistributions of any program source code that utilizes The Sims 2 Assets must also retain the copyright notice, list of conditions and the disclaimer contained in The Alice 3.0 Art Gallery License.
 * 
 * DISCLAIMER:
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.  ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 */

package org.lgna.story.resources.sims2;

public class FullBodyOutfitManager extends IngredientManager<FullBodyOutfit> {
	private static FullBodyOutfitManager singleton = new FullBodyOutfitManager();

	public static FullBodyOutfitManager getSingleton() {
		return singleton;
	}

	private FullBodyOutfitManager() {
		this.add( MaleTeenFullBodyOutfit.class, 
				MaleTeenFullBodyOutfitClosedCoatLongPants.class,
				MaleTeenFullBodyOutfitHiphopBaggy.class,
				MaleTeenFullBodyOutfitHipJersey.class,
				MaleTeenFullBodyOutfitHipOpenShirtOverPants.class,
				MaleTeenFullBodyOutfitHipSloppyVsweater.class,
				MaleTeenFullBodyOutfitHoodedSweatShirtBoardShorts.class,
				MaleTeenFullBodyOutfitHoodedSweatShirtPants.class,
				MaleTeenFullBodyOutfitKilt.class,
				MaleTeenFullBodyOutfitLabcoat.class,
				MaleTeenFullBodyOutfitOpenCoatLongPants.class,
				MaleTeenFullBodyOutfitOpenShirtOverPants.class,
				MaleTeenFullBodyOutfitOverhangTshirt.class,
				MaleTeenFullBodyOutfitOverhangTshirtLongShorts.class,
				MaleTeenFullBodyOutfitPajamas.class,
				MaleTeenFullBodyOutfitPajamasBoxers.class,
				MaleTeenFullBodyOutfitPoloShirtOverhang.class,
				MaleTeenFullBodyOutfitPowerSuit.class,
				MaleTeenFullBodyOutfitPunk.class,
				MaleTeenFullBodyOutfitShorts.class,
				MaleTeenFullBodyOutfitSloppySuit.class,
				MaleTeenFullBodyOutfitSoldier.class,
				MaleTeenFullBodyOutfitSuit.class,
				MaleTeenFullBodyOutfitSwimShort.class,
				MaleTeenFullBodyOutfitTightPantsLooseShirt.class,
				MaleTeenFullBodyOutfitTrackSuit.class,
				MaleTeenFullBodyOutfitTrenchCoatPantsBoots.class,
				MaleTeenFullBodyOutfitServer.class,
				MaleTeenFullBodyOutfitScrubs.class,
				MaleTeenFullBodyOutfitOrderly.class,
				MaleTeenFullBodyOutfitMechanic.class,
				MaleTeenFullBodyOutfitFastFood.class,
				MaleTeenFullBodyOutfitCop.class,
				MaleTeenFullBodyOutfitClerk.class,
				MaleTeenFullBodyOutfitApron.class,
				MaleTeenFullBodyOutfitAmbulanceDriver.class
				);
		this.add( FemaleTeenFullBodyOutfit.class, 
				FemaleTeenFullBodyOutfitAthleticJersey.class,
				FemaleTeenFullBodyOutfitBlazerPleats.class,
				FemaleTeenFullBodyOutfitCheerleader.class,
				FemaleTeenFullBodyOutfitCoutureShortDress.class,
				FemaleTeenFullBodyOutfitDressAboveKneeSuit.class,
				FemaleTeenFullBodyOutfitDressBeltBoots.class,
				FemaleTeenFullBodyOutfitDressFormalTrumpet.class,
				FemaleTeenFullBodyOutfitDressKnee.class,
				FemaleTeenFullBodyOutfitDressWrapArm.class,
				FemaleTeenFullBodyOutfitFlaresFlowTank.class,
				FemaleTeenFullBodyOutfitFormalDress.class,
				FemaleTeenFullBodyOutfitGothSpikeNeck.class,
				FemaleTeenFullBodyOutfitHipBlazerFlaredPants.class,
				FemaleTeenFullBodyOutfitHipBootIsCool.class,
				FemaleTeenFullBodyOutfitHipJacketWideFlaredPants.class,
				FemaleTeenFullBodyOutfitHipMandarinTop.class,
				FemaleTeenFullBodyOutfitHipMicroMiniSkirt.class,
				FemaleTeenFullBodyOutfitHipSweats.class,
				FemaleTeenFullBodyOutfitJumpSuitBoots.class,
				FemaleTeenFullBodyOutfitLabcoat.class,
				FemaleTeenFullBodyOutfitNaked.class,
				FemaleTeenFullBodyOutfitNiteShirt.class,
				FemaleTeenFullBodyOutfitNYC2.class,
				FemaleTeenFullBodyOutfitPajamasClassic.class,
				FemaleTeenFullBodyOutfitPleatsOutfit.class,
				FemaleTeenFullBodyOutfitPowerSuit.class,
				FemaleTeenFullBodyOutfitPunk.class,
				FemaleTeenFullBodyOutfitSoldier.class,
				FemaleTeenFullBodyOutfitSundress.class,
				FemaleTeenFullBodyOutfitSwimwear.class,
				FemaleTeenFullBodyOutfitTracksuit.class,
				FemaleTeenFullBodyOutfitUnderwear.class,
				FemaleTeenFullBodyOutfitServer.class,
				FemaleTeenFullBodyOutfitScrubs.class,
				FemaleTeenFullBodyOutfitOrderly.class,
				FemaleTeenFullBodyOutfitMechanic.class,
				FemaleTeenFullBodyOutfitFastFood.class,
				FemaleTeenFullBodyOutfitCop.class,
				FemaleTeenFullBodyOutfitClerk.class,
				FemaleTeenFullBodyOutfitApron.class,
				FemaleTeenFullBodyOutfitAmbulanceDriver.class
				);
		this.add( MaleAdultFullBodyOutfit.class, 
				MaleAdultFullBodyOutfitChef.class,
				MaleAdultFullBodyOutfitClosedCoatLongPants.class,
				MaleAdultFullBodyOutfitCoach.class,
				MaleAdultFullBodyOutfitCouture.class,
				MaleAdultFullBodyOutfitDressKorean.class,
				MaleAdultFullBodyOutfitFancySuit.class,
				MaleAdultFullBodyOutfitGenerals.class,
				MaleAdultFullBodyOutfitGothTeeShirt.class,
				MaleAdultFullBodyOutfitHiphopHood.class,
				MaleAdultFullBodyOutfitHoodedSweatShirtBoardShorts.class,
				MaleAdultFullBodyOutfitHoodedSweatShirtPants.class,
				MaleAdultFullBodyOutfitKilt.class,
				MaleAdultFullBodyOutfitLabcoat.class,
				MaleAdultFullBodyOutfitLongCoat.class,
				MaleAdultFullBodyOutfitLooseOpenCoatPants.class,
				MaleAdultFullBodyOutfitMadScientist.class,
				MaleAdultFullBodyOutfitManMaid.class,
				MaleAdultFullBodyOutfitMaternityComfy.class,
				MaleAdultFullBodyOutfitMayor.class,
				MaleAdultFullBodyOutfitMRacer.class,
				MaleAdultFullBodyOutfitOpenCoatLongPants.class,
				MaleAdultFullBodyOutfitOpenShirtPants.class,
				MaleAdultFullBodyOutfitOpenSportcoatLongPants.class,
				MaleAdultFullBodyOutfitOveralls.class,
				MaleAdultFullBodyOutfitOverhangTshirt.class,
				MaleAdultFullBodyOutfitOverhangTshirtGamespot.class,
				MaleAdultFullBodyOutfitOverhangTshirtGamespy.class,
				MaleAdultFullBodyOutfitOverhangTshirtLongShorts.class,
				MaleAdultFullBodyOutfitOverShirtPantsShoes.class,
				MaleAdultFullBodyOutfitPajamas.class,
				MaleAdultFullBodyOutfitPajamasBoxers.class,
				MaleAdultFullBodyOutfitPajamasDrawstring.class,
				MaleAdultFullBodyOutfitPirate.class,
				MaleAdultFullBodyOutfitPowerSuit.class,
				MaleAdultFullBodyOutfitPunk.class,
				MaleAdultFullBodyOutfitShirtFlares.class,
				MaleAdultFullBodyOutfitShirtUntuckedSaddle.class,
				MaleAdultFullBodyOutfitShorts.class,
				MaleAdultFullBodyOutfitSlickSuit.class,
				MaleAdultFullBodyOutfitSloppySuit.class,
				MaleAdultFullBodyOutfitSoldier.class,
				MaleAdultFullBodyOutfitSuit.class,
				MaleAdultFullBodyOutfitSuperChef.class,
				MaleAdultFullBodyOutfitSuperHero.class,
				MaleAdultFullBodyOutfitSuperVillain.class,
				MaleAdultFullBodyOutfitSwat.class,
				MaleAdultFullBodyOutfitSweats.class,
				MaleAdultFullBodyOutfitTrackSuit.class,
				MaleAdultFullBodyOutfitTrenchCoatPantsBoots.class,
				MaleAdultFullBodyOutfitTweedJacket.class,
				MaleAdultFullBodyOutfitUnderwear.class,
				MaleAdultFullBodyOutfitApron.class,
				MaleAdultFullBodyOutfitShrink.class,
				MaleAdultFullBodyOutfitServer.class,
				MaleAdultFullBodyOutfitScrubs.class,
				MaleAdultFullBodyOutfitPizzaDelivery.class,
				MaleAdultFullBodyOutfitOrderly.class,
				MaleAdultFullBodyOutfitMechanic.class,
				MaleAdultFullBodyOutfitGardener.class,
				MaleAdultFullBodyOutfitFirefighter.class,
				MaleAdultFullBodyOutfitFastFood.class,
				MaleAdultFullBodyOutfitExterminator.class,
				MaleAdultFullBodyOutfitDeliveryPerson.class,
				MaleAdultFullBodyOutfitCop.class,
				MaleAdultFullBodyOutfitClerk.class,
				MaleAdultFullBodyOutfitBusDriver.class,
				MaleAdultFullBodyOutfitBurglar.class,
				MaleAdultFullBodyOutfitBartender.class,
				AdultFullBodyOutfitAstronaut.class,
				MaleAdultFullBodyOutfitAmbulanceDriver.class
				);
		this.add( FemaleAdultFullBodyOutfit.class, 
				FemaleAdultFullBodyOutfitChef.class,
				FemaleAdultFullBodyOutfitCoach.class,
				FemaleAdultFullBodyOutfitDress.class,
				FemaleAdultFullBodyOutfitDressAboveKnee.class,
				FemaleAdultFullBodyOutfitDressAboveKneeHooded.class,
				FemaleAdultFullBodyOutfitDressAboveKneeSuit.class,
				FemaleAdultFullBodyOutfitDressChina.class,
				FemaleAdultFullBodyOutfitDressFormalLong.class,
				FemaleAdultFullBodyOutfitDressGothic.class,
				FemaleAdultFullBodyOutfitDressKorean.class,
				FemaleAdultFullBodyOutfitDressLongHug.class,
				FemaleAdultFullBodyOutfitDressLongLoose.class,
				FemaleAdultFullBodyOutfitDressLongTwo.class,
				FemaleAdultFullBodyOutfitFurCoatShortDressShoes.class,
				FemaleAdultFullBodyOutfitJacketHighCollar.class,
				FemaleAdultFullBodyOutfitJacketShortDressBoots.class,
				FemaleAdultFullBodyOutfitJacketShortDressShoes.class,
				FemaleAdultFullBodyOutfitJacketShortDressSlitShoes.class,
				FemaleAdultFullBodyOutfitJacketTurtleSweaterDressBoots.class,
				FemaleAdultFullBodyOutfitKTDream.class,
				FemaleAdultFullBodyOutfitLabcoat.class,
				FemaleAdultFullBodyOutfitLeatherJacket.class,
				FemaleAdultFullBodyOutfitMadScientist.class,
				FemaleAdultFullBodyOutfitMaternityShirtPants.class,
				FemaleAdultFullBodyOutfitMayor.class,
				FemaleAdultFullBodyOutfitMilitaryOfficer.class,
				FemaleAdultFullBodyOutfitMRacer.class,
				FemaleAdultFullBodyOutfitNaked.class,
				FemaleAdultFullBodyOutfitNightgown.class,
				FemaleAdultFullBodyOutfitOverShirtPantsSandals.class,
				FemaleAdultFullBodyOutfitPajamasClassic.class,
				FemaleAdultFullBodyOutfitPirate.class,
				FemaleAdultFullBodyOutfitPowerSuit.class,
				FemaleAdultFullBodyOutfitShirtUntuckedOxford.class,
				FemaleAdultFullBodyOutfitShortDressBoots.class,
				FemaleAdultFullBodyOutfitShortDressShoes.class,
				FemaleAdultFullBodyOutfitSlickSuit.class,
				FemaleAdultFullBodyOutfitSoldier.class,
				FemaleAdultFullBodyOutfitSuit.class,
				FemaleAdultFullBodyOutfitSundress.class,
				FemaleAdultFullBodyOutfitSuperChef.class,
				FemaleAdultFullBodyOutfitSuperHero.class,
				FemaleAdultFullBodyOutfitSuperVillain.class,
				FemaleAdultFullBodyOutfitSuspendersTights.class,
				FemaleAdultFullBodyOutfitSwat.class,
				FemaleAdultFullBodyOutfitSweats.class,
				FemaleAdultFullBodyOutfitSwimwear.class,
				FemaleAdultFullBodyOutfitSwimwearCleavage.class,
				FemaleAdultFullBodyOutfitSwimwearSporty.class,
				FemaleAdultFullBodyOutfitTracksuit.class,
				FemaleAdultFullBodyOutfitTurtleSweaterDressShoes.class,
				FemaleAdultFullBodyOutfitTweedJacket.class,
				FemaleAdultFullBodyOutfitUnderwear.class,
				FemaleAdultFullBodyOutfitWarrior.class,
				FemaleAdultFullBodyOutfitApron.class,
				FemaleAdultFullBodyOutfitSocialWorker.class,
				FemaleAdultFullBodyOutfitServer.class,
				FemaleAdultFullBodyOutfitScrubs.class,
				FemaleAdultFullBodyOutfitPizzaDelivery.class,
				FemaleAdultFullBodyOutfitOrderly.class,
				FemaleAdultFullBodyOutfitMechanic.class,
				FemaleAdultFullBodyOutfitDressMaid.class,
				FemaleAdultFullBodyOutfitGardener.class,
				FemaleAdultFullBodyOutfitFirefighter.class,
				FemaleAdultFullBodyOutfitFastFood.class,
				FemaleAdultFullBodyOutfitDeliveryPerson.class,
				FemaleAdultFullBodyOutfitCop.class,
				FemaleAdultFullBodyOutfitClerk.class,
				FemaleAdultFullBodyOutfitBusDriver.class,
				FemaleAdultFullBodyOutfitBurglar.class,
				FemaleAdultFullBodyOutfitBartender.class,
				AdultFullBodyOutfitAstronaut.class,
				FemaleAdultFullBodyOutfitAmbulanceDriver.class
				);
		this.add( FemaleToddlerFullBodyOutfit.class, 
				FemaleToddlerFullBodyOutfitDress.class,
				FemaleToddlerFullBodyOutfitDressClosedSleeves.class,
				FemaleToddlerFullBodyOutfitOpenCoatDressShoes.class,
				FemaleToddlerFullBodyOutfitShortSleevesLongPants.class,
				FemaleToddlerFullBodyOutfitSundress.class,
				FemaleToddlerFullBodyOutfitSundressSleeves.class,
				ToddlerFullBodyOutfitDiaper.class,
				ToddlerFullBodyOutfitJumpCollar.class,
				ToddlerFullBodyOutfitNaked.class,
				ToddlerFullBodyOutfitOnePieceShortsCollar.class,
				ToddlerFullBodyOutfitSleeper.class
				);
		this.add( MaleToddlerFullBodyOutfit.class, 
				MaleToddlerFullBodyOutfitJacketPants.class,
				MaleToddlerFullBodyOutfitPantsCollar.class,
				ToddlerFullBodyOutfitDiaper.class,
				ToddlerFullBodyOutfitJumpCollar.class,
				ToddlerFullBodyOutfitNaked.class,
				ToddlerFullBodyOutfitOnePieceShortsCollar.class,
				ToddlerFullBodyOutfitSleeper.class
				);
		this.add( MaleChildFullBodyOutfit.class, 
				MaleChildFullBodyOutfitBigShorts.class,
				MaleChildFullBodyOutfitJacketShortsBoots.class,
				MaleChildFullBodyOutfitLongSweaterPants.class,
				MaleChildFullBodyOutfitOpenCoatLongPants.class,
				MaleChildFullBodyOutfitOverShirtShorts.class,
				MaleChildFullBodyOutfitPulloverShirtPants.class,
				MaleChildFullBodyOutfitShirtOverPants.class,
				MaleChildFullBodyOutfitSportif.class,
				ChildFullBodyOutfitJumper.class,
				ChildFullBodyOutfitNaked.class,
				ChildFullBodyOutfitPirate.class,
				ChildFullBodyOutfitPuffyPJ.class
				);
		this.add( FemaleChildFullBodyOutfit.class, 
				FemaleChildFullBodyOutfitBlazerPleats.class,
				FemaleChildFullBodyOutfitCowGirlSkirt.class,
				FemaleChildFullBodyOutfitDressAboveKneeCollar.class,
				FemaleChildFullBodyOutfitDressFormal.class,
				FemaleChildFullBodyOutfitDressShortBell.class,
				FemaleChildFullBodyOutfitFairy.class,
				FemaleChildFullBodyOutfitJacketDressFlare.class,
				FemaleChildFullBodyOutfitTShirtPants.class,
				ChildFullBodyOutfitJumper.class,
				ChildFullBodyOutfitNaked.class,
				ChildFullBodyOutfitPirate.class,
				ChildFullBodyOutfitPuffyPJ.class
				);
	}

	@Override
	protected Class<Class<? extends FullBodyOutfit>> getImplementingClassesComponentType() {
		return (Class<Class<? extends FullBodyOutfit>>)FullBodyOutfit.class.getClass();
	}

	@Override
	protected Class<? extends FullBodyOutfit> getGenderedInterfaceClass( LifeStage lifeStage, Gender gender ) {
		return lifeStage.getGenderedFullBodyOutfitInterfaceClass( gender );
	}
}

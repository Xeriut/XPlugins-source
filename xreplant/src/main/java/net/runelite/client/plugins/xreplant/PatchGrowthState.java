/*
 * Copyright (c) 2019 Abex
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *     list of conditions and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above copyright notice,
 *     this list of conditions and the following disclaimer in the documentation
 *     and/or other materials provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 *  ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.client.plugins.xreplant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.plugins.timetracking.Tab;
import net.runelite.client.plugins.timetracking.farming.CropState;
import net.runelite.client.plugins.timetracking.farming.Produce;

import javax.annotation.Nullable;

@RequiredArgsConstructor
@Getter
@Slf4j
public enum PatchGrowthState
{
	FRUIT_TREE()
		{
				@Override
				public PatchState forVarbitValue(int value)
				{
					if (value >= 0 && value < 3)
					{
						// Fruit Tree Patch[Rake,Inspect,Guide] 8050,8049,8048,8047
						return PatchState.RAKE;
					}
					if (value == 3)
					{
						// Fruit Tree Patch[Rake,Inspect,Guide] 8050,8049,8048,8047
						return PatchState.PLACE;
					}
					if (value >= 4 && value <= 7)
					{
						// Fruit Tree Patch[Rake,Inspect,Guide] 8050,8050,8050,8050
						return PatchState.RAKE;
					}
					if (value >= 14 && value <= 20)
					{
						// Apple tree[Chop-down,Inspect,Guide,Pick-apple] 7941,7942,7943,7944,7945,7946,7947
						return PatchState.HARVEST;
					}
					if (value >= 21 && value <= 26)
					{
						// Diseased apple tree[Prune,Inspect,Guide] 7949,7950,7951,7952,7953,7954
						return PatchState.HARVEST;
					}
					if (value >= 27 && value <= 32)
					{
						// Dead apple tree[Clear,Inspect,Guide] 7955,7956,7957,7958,7959,7960
						return PatchState.HARVEST;
					}
					if (value == 33)
					{
						// Apple tree stump[Clear,Inspect,Guide] 7961
						return PatchState.HARVEST;
					}
					if (value == 34)
					{
						// Apple tree[Check-health,Inspect,Guide] 7948
						return PatchState.CHECK;
					}
					if (value >= 41 && value <= 47)
					{
						// Banana tree[Chop-down,Inspect,Guide,Pick-banana] 8000,8001,8002,8003,8004,8005,8006
						return PatchState.HARVEST;
					}
					if (value >= 48 && value <= 53)
					{
						// Diseased banana tree[Prune,Inspect,Guide] 8007,8008,8009,8010,8011,8012
						return PatchState.HARVEST;
					}
					if (value >= 54 && value <= 59)
					{
						// Dead banana tree[Clear,Inspect,Guide] 8013,8014,8015,8016,8017,8018
						return PatchState.HARVEST;
					}
					if (value == 60)
					{
						// Banana tree stump[Clear,Inspect,Guide] 8019
						return PatchState.HARVEST;
					}
					if (value == 61)
					{
						// Banana tree[Check-health,Inspect,Guide] 7999
						return PatchState.CHECK;
					}
					if (value >= 62 && value <= 71)
					{
						// Fruit Tree Patch[Rake,Inspect,Guide] 8050,8050,8050,8050,8050,8050,8050,8050,8050,8050
						return PatchState.RAKE;
					}
					if (value >= 78 && value <= 84)
					{
						// Orange tree[Chop-down,Inspect,Guide,Pick-orange] 8057,8058,8059,8060,8061,8062,8063
						return PatchState.HARVEST;
					}
					if (value >= 85 && value <= 89)
					{
						// Diseased orange tree[Prune,Inspect,Guide] 8065,8066,8067,8068,8069
						return PatchState.HARVEST;
					}
					if (value == 90)
					{
						// Diseased orange tree[Chop-down,Inspect,Guide] 8070
						return PatchState.HARVEST;
					}
					if (value >= 91 && value <= 96)
					{
						// Dead orange tree[Clear,Inspect,Guide] 8071,8072,8073,8074,8075,8076
						return PatchState.HARVEST;
					}
					if (value == 97)
					{
						// Orange tree stump[Clear,Inspect,Guide] 8077
						return PatchState.HARVEST;
					}
					if (value == 98)
					{
						// Orange tree[Check-health,Inspect,Guide] 8064
						return PatchState.CHECK;
					}
					if (value >= 105 && value <= 111)
					{
						// Curry tree[Chop-down,Inspect,Guide,Pick-leaf] 8026,8027,8028,8029,8030,8031,8032
						return PatchState.HARVEST;
					}
					if (value >= 112 && value <= 117)
					{
						// Diseased curry tree[Prune,Inspect,Guide] 8034,8035,8036,8037,8038,8039
						return PatchState.HARVEST;
					}
					if (value >= 118 && value <= 123)
					{
						// Dead curry tree[Clear,Inspect,Guide] 8040,8041,8042,8043,8044,8045
						return PatchState.HARVEST;
					}
					if (value == 124)
					{
						// Curry tree stump[Clear,Inspect,Guide] 8046
						return PatchState.HARVEST;
					}
					if (value == 125)
					{
						// Curry tree[Check-health,Inspect,Guide] 8033
						return PatchState.CHECK;
					}
					if (value >= 126 && value <= 135)
					{
						// Fruit Tree Patch[Rake,Inspect,Guide] 8050,8050,8050,8050,8050,8050,8050,8050,8050,8050
						return PatchState.RAKE;
					}
					if (value >= 142 && value <= 148)
					{
						// Pineapple plant[Chop down,Inspect,Guide,Pick-pineapple] 7972,7973,7974,7975,7976,7977,7978
						return PatchState.HARVEST;
					}
					if (value >= 149 && value <= 154)
					{
						// Diseased pineapple plant[Prune,Inspect,Guide] 7980,7981,7982,7983,7984,7985
						return PatchState.HARVEST;
					}
					if (value >= 155 && value <= 160)
					{
						// Dead pineapple plant[Clear,Inspect,Guide] 7986,7987,7988,7989,7990,7991
						return PatchState.HARVEST;
					}
					if (value == 161)
					{
						// Pineapple plant stump[Clear,Inspect,Guide] 7992
						return PatchState.HARVEST;
					}
					if (value == 162)
					{
						// Pineapple plant[Check-health,Inspect,Guide] 7979
						return PatchState.CHECK;
					}
					if (value >= 169 && value <= 175)
					{
						// Papaya tree[Chop-down,Inspect,Guide,Pick-fruit] 8111,8112,8113,8114,8115,8116,8117
						return PatchState.HARVEST;
					}
					if (value >= 176 && value <= 181)
					{
						// Diseased papaya tree[Prune,Inspect,Guide] 8119,8120,8121,8122,8123,8124
						return PatchState.HARVEST;
					}
					if (value >= 182 && value <= 187)
					{
						// Dead papaya tree[Clear,Inspect,Guide] 8125,8126,8127,8128,8129,8130
						return PatchState.HARVEST;
					}
					if (value == 188)
					{
						// Papaya tree stump[Clear,Inspect,Guide] 8131
						return PatchState.HARVEST;
					}
					if (value == 189)
					{
						// Papaya tree[Check-health,Inspect,Guide] 8118
						return PatchState.CHECK;
					}
					if (value >= 190 && value <= 199)
					{
						// Fruit Tree Patch[Rake,Inspect,Guide] 8050,8050,8050,8050,8050,8050,8050,8050,8050,8050
						return PatchState.HARVEST;
					}
					if (value >= 206 && value <= 212)
					{
						// Palm tree[Chop-down,Inspect,Guide,Pick-coconut] 8084,8085,8086,8087,8088,8089,8090
						return PatchState.HARVEST;
					}
					if (value >= 213 && value <= 218)
					{
						// Diseased palm tree[Prune,Inspect,Guide] 8092,8093,8094,8095,8096,8097
						return PatchState.HARVEST;
					}
					if (value >= 219 && value <= 224)
					{
						// Dead palm tree[Clear,Inspect,Guide] 8098,8099,8100,8101,8102,8103
						return PatchState.HARVEST;
					}
					if (value == 225)
					{
						// Palm tree stump[Clear,Inspect,Guide] 8104
						return PatchState.HARVEST;
					}
					if (value == 226)
					{
						// Palm tree[Check-health,Inspect,Guide] 8091
						return PatchState.CHECK;
					}
					if (value >= 233 && value <= 239)
					{
						// Dragonfruit tree[Chop down,Inspect,Guide,Pick-dragonfruit] 34014,34015,34016,34017,34018,34019,34020
						return PatchState.HARVEST;
					}
					if (value >= 240 && value <= 245)
					{
						// Diseased dragonfruit plant[Prune,Inspect,Guide] 34022,34023,34024,34025,34026,34027
						return PatchState.HARVEST;
					}
					if (value >= 246 && value <= 251)
					{
						// Dead dragonfruit plant[Clear,Inspect,Guide] 34028,34029,34030,34031,34032,34033
						return PatchState.HARVEST;
					}
					if (value == 252)
					{
						// Dragonfruit tree stump[Clear,Inspect,Guide] 34034
						return PatchState.HARVEST;
					}
					if (value == 253)
					{
						// Dragonfruit tree[Check-health,Inspect,Guide] 34021
						return PatchState.CHECK;
					}
					if (value >= 254 && value <= 255)
					{
						// Fruit Tree Patch[Rake,Inspect,Guide] 8050,8050
						return PatchState.RAKE;
					}
					return null;
				}
				},
	TREE()
		{
			@Override
			public PatchState forVarbitValue(int value)
			{
				if (value >= 0 && value < 3)
				{
					// Tree patch[Rake,Inspect,Guide] 8395,8394,8393,8392
					return PatchState.RAKE;
				}
				if(value == 3) {
					return PatchState.PLACE;
				}
				if (value >= 4 && value <= 7)
				{
					// Tree patch[Rake,Inspect,Guide] 8395,8395,8395,8395
					return PatchState.RAKE;
				}
				if (value == 12)
				{
					// Oak[Check-health,Inspect,Guide] 8466
					return PatchState.CHECK;
				}
				if (value == 13)
				{
					// Oak[Chop down,Inspect,Guide] 8467
					return PatchState.HARVEST;
				}
				if (value == 14)
				{
					// Oak tree stump[Clear,Inspect,Guide] 8468
					return PatchState.HARVEST;
				}
				if (value == 21)
				{
					// Willow Tree[Check-health,Inspect,Guide] 8487
					return PatchState.CHECK;
				}
				if (value == 22)
				{
					// Willow Tree[Chop down,Inspect,Guide] 8488
					return PatchState.HARVEST;
				}
				if (value == 23)
				{
					// Willow tree stump[Clear,Inspect,Guide] 8489
					return PatchState.HARVEST;
				}
				if (value == 32)
				{
					// Maple Tree[Check-health,Inspect,Guide] 8443
					return PatchState.CHECK;
				}
				if (value == 33)
				{
					// Maple Tree[Chop down,Inspect,Guide] 8444
					return PatchState.HARVEST;
				}
				if (value == 34)
				{
					// Tree stump[Clear,Inspect,Guide] 8445
					return PatchState.HARVEST;
				}
				if (value == 45)
				{
					// Yew tree[Check-health,Inspect,Guide] 8512
					return PatchState.CHECK;
				}
				if (value == 46)
				{
					// Yew tree[Chop down,Inspect,Guide] 8513
					return PatchState.HARVEST;
				}
				if (value == 47)
				{
					// Yew tree stump[Clear,Inspect,Guide] 8514
					return PatchState.HARVEST;
				}
				if (value == 60)
				{
					// Magic Tree[Check-health,Inspect,Guide] 8408
					return PatchState.CHECK;
				}
				if (value == 61)
				{
					// Magic Tree[Chop down,Inspect,Guide] 8409
					return PatchState.HARVEST;
				}
				if (value == 62)
				{
					// Magic Tree Stump[Clear,Inspect,Guide] 8410
					return PatchState.HARVEST;
				}
				if (value >= 63 && value <= 72)
				{
					// Tree patch[Rake,Inspect,Guide] 8395,8395,8395,8395,8395,8395,8395,8395,8395,8395
					return PatchState.RAKE;
				}
				if (value >= 73 && value <= 75)
				{
					// Diseased Oak[Prune,Inspect,Guide] 8473,8474,8475
					return PatchState.HARVEST;
				}
				if (value == 77)
				{
					// Diseased Oak[Prune,Inspect,Guide] 8476
					return PatchState.HARVEST;
				}
				if (value >= 78 && value <= 79)
				{
					// Tree patch[Rake,Inspect,Guide] 8395,8395
					return PatchState.RAKE;
				}
				if (value >= 80 && value <= 84)
				{
					// Diseased Willow[Prune,Inspect,Guide] 8490,8491,8492,8493,8494
					return PatchState.HARVEST;
				}
				if (value == 86)
				{
					// Diseased Willow[Prune,Inspect,Guide] 8495
					return PatchState.HARVEST;
				}
				if (value >= 87 && value <= 88)
				{
					// Tree patch[Rake,Inspect,Guide] 8395,8395
					return PatchState.RAKE;
				}
				if (value >= 89 && value <= 95)
				{
					// Diseased Maple[Prune,Inspect,Guide] 8446,8447,8448,8449,8450,8451,8452
					return PatchState.HARVEST;
				}
				if (value == 97)
				{
					// Diseased Maple[Prune,Inspect,Guide] 8453
					return PatchState.HARVEST;
				}
				if (value >= 98 && value <= 99)
				{
					// Tree patch[Rake,Inspect,Guide] 8395,8395
					return PatchState.RAKE;
				}
				if (value >= 100 && value <= 108)
				{
					// Diseased Yew[Prune,Inspect,Guide] 8515,8516,8517,8518,8519,8520,8521,8522,8523
					return PatchState.HARVEST;
				}
				if (value == 110)
				{
					// Diseased Yew[Prune,Inspect,Guide] 8524
					return PatchState.HARVEST;
				}
				if (value >= 111 && value <= 112)
				{
					// Tree patch[Rake,Inspect,Guide] 8395,8395
					return PatchState.RAKE;
				}
				if (value >= 113 && value <= 123)
				{
					// Diseased Magic Tree[Prune,Inspect,Guide] 8411,8412,8413,8414,8415,8416,8417,8418,8419,8420,8421
					return PatchState.HARVEST;
				}
				if (value == 125)
				{
					// Diseased Magic Tree[Prune,Inspect,Guide] 8422
					return PatchState.HARVEST;
				}
				if (value >= 126 && value <= 136)
				{
					// Tree patch[Rake,Inspect,Guide] 8395,8395,8395,8395,8395,8395,8395,8395,8395,8395,8395
					return PatchState.RAKE;
				}
				if (value >= 137 && value <= 139)
				{
					// Dead Oak[Clear,Inspect,Guide] 8477,8478,8479
					return PatchState.HARVEST;
				}
				if (value == 141)
				{
					// Dead Oak[Clear,Inspect,Guide] 8480
					return PatchState.HARVEST;
				}
				if (value >= 142 && value <= 143)
				{
					// Tree patch[Rake,Inspect,Guide] 8395,8395
					return PatchState.RAKE;
				}
				if (value >= 144 && value <= 148)
				{
					// Dead Willow[Clear,Inspect,Guide] 8496,8497,8498,8499,8500
					return PatchState.HARVEST;
				}
				if (value == 150)
				{
					// Dead Willow[Clear,Inspect,Guide] 8501
					return PatchState.HARVEST;
				}
				if (value >= 151 && value <= 152)
				{
					// Tree patch[Rake,Inspect,Guide] 8395,8395
					return PatchState.RAKE;
				}
				if (value >= 153 && value <= 159)
				{
					// Dead Maple[Clear,Inspect,Guide] 8454,8455,8456,8457,8458,8459,8460
					return PatchState.HARVEST;
				}
				if (value == 161)
				{
					// Dead Maple[Clear,Inspect,Guide] 8461
					return PatchState.HARVEST;
				}
				if (value >= 162 && value <= 163)
				{
					// Tree patch[Rake,Inspect,Guide] 8395,8395
					return PatchState.RAKE;
				}
				if (value >= 164 && value <= 172)
				{
					// Dead Yew[Clear,Inspect,Guide] 8525,8526,8527,8528,8529,8530,8531,8532,8533
					return PatchState.HARVEST;
				}
				if (value == 174)
				{
					// Dead Yew[Clear,Inspect,Guide] 8534
					return PatchState.HARVEST;
				}
				if (value >= 175 && value <= 176)
				{
					// Tree patch[Rake,Inspect,Guide] 8395,8395
					return PatchState.RAKE;
				}
				if (value >= 177 && value <= 187)
				{
					// Dead Magic Tree[Clear,Inspect,Guide] 8423,8424,8425,8426,8427,8428,8429,8430,8431,8432,8433
					return PatchState.HARVEST;
				}
				if (value == 189)
				{
					// Dead Magic Tree[Clear,Inspect,Guide] 8434
					return PatchState.HARVEST;
				}
				if (value >= 190 && value <= 191)
				{
					// Tree patch[Rake,Inspect,Guide] 8395,8395
					return PatchState.RAKE;
				}
				if (value >= 192 && value <= 197)
				{
					// Willow Tree[Chop down,Inspect,Guide] 8488,8488,8488,8488,8488,8488
					return PatchState.HARVEST;
				}
				if (value >= 198 && value <= 255)
				{
					// Tree patch[Rake,Inspect,Guide] 8395,8395,8395,8395,8395,8395,8395,8395,8395,8395,8395,8395,8395,8395,8395,8395,8395,8395,8395,8395,8395,8395,8395,8395,8395,8395,8395,8395,8395,8395,8395,8395,8395,8395,8395,8395,8395,8395,8395,8395,8395,8395,8395,8395,8395,8395,8395,8395,8395,8395,8395,8395,8395,8395,8395,8395,8395,8395
					return PatchState.RAKE;
				}
				return null;
			}
		};

	@Nullable
	public abstract PatchState forVarbitValue(int value);

}

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

import javax.annotation.Nullable;

@RequiredArgsConstructor
@Getter
@Slf4j
public enum PatchGrowthState
{
	TREE()
		{
			@Override
			public PatchState forVarbitValue(int value)
			{
				log.info("varbit val" + value);
				if (value >= 0 && value < 3)
				{
					// Tree patch[Rake,Inspect,Guide] 8395,8394,8393,8392
					return PatchState.RAKE;
				}
				if (value == 3)
				{
					// Tree patch[Inspect,Guide] Empty patch
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
				if (value >= 63 && value <= 72)
				{
					// Tree patch[Rake,Inspect,Guide] 8395,8395,8395,8395,8395,8395,8395,8395,8395,8395
					return PatchState.RAKE;
				}
				if (value >= 78 && value <= 79)
				{
					// Tree patch[Rake,Inspect,Guide] 8395,8395
					return PatchState.RAKE;
				}
				if (value >= 87 && value <= 88)
				{
					// Tree patch[Rake,Inspect,Guide] 8395,8395
					return PatchState.RAKE;
				}
				if (value >= 98 && value <= 99)
				{
					// Tree patch[Rake,Inspect,Guide] 8395,8395
					return PatchState.RAKE;
				}
				if (value >= 111 && value <= 112)
				{
					// Tree patch[Rake,Inspect,Guide] 8395,8395
					return PatchState.RAKE;
				}
				if (value >= 126 && value <= 136)
				{
					// Tree patch[Rake,Inspect,Guide] 8395,8395,8395,8395,8395,8395,8395,8395,8395,8395,8395
					return PatchState.RAKE;
				}
				if (value >= 142 && value <= 143)
				{
					// Tree patch[Rake,Inspect,Guide] 8395,8395
					return PatchState.RAKE;
				}
				if (value >= 151 && value <= 152)
				{
					// Tree patch[Rake,Inspect,Guide] 8395,8395
					return PatchState.RAKE;
				}
				if (value >= 162 && value <= 163)
				{
					// Tree patch[Rake,Inspect,Guide] 8395,8395
					return PatchState.RAKE;
				}
				if (value >= 175 && value <= 176)
				{
					// Tree patch[Rake,Inspect,Guide] 8395,8395
					return PatchState.RAKE;
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

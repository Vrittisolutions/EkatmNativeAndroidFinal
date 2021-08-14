uniform highp vec4 screenSize;
uniform highp mat4 matWorldViewProj;
uniform highp vec4 WaterFallTexCoordCalcParams;
uniform highp vec4 WaterFallSliceOffsset;
uniform highp vec4 TextureDimensionsInv;
uniform highp vec4 WorldXZOffsetSize;
uniform sampler2D HeightMapTexture;
attribute highp vec4 vPosition;
attribute highp vec4 vNormal;
attribute highp vec3 vTexCoord0;
attribute highp vec4 vTexCoord1;
attribute highp vec4 vColor;
attribute highp vec4 vColor1;
varying highp vec4 xlv_COLOR0;
varying highp vec2 xlv_TEXCOORD0;
varying highp vec2 xlv_TEXCOORD1;
void main ()
{
  highp vec4 tmpvar_1;
  highp vec4 tmpvar_2;
  highp vec4 tmpvar_3;
  highp vec4 tmpvar_4;
  tmpvar_4 = mix (vPosition, vNormal, vTexCoord1.yyyy);
  highp vec4 tmpvar_5;
  tmpvar_5 = mix (vNormal, vPosition, vTexCoord1.yyyy);
  highp vec3 tmpvar_6;
  tmpvar_6.x = (vTexCoord0.x * vTexCoord1.z);
  tmpvar_6.y = vTexCoord1.x;
  tmpvar_6.z = vTexCoord0.z;
  tmpvar_1.xyz = tmpvar_4.xyz;
  tmpvar_1.w = 1.0;
  tmpvar_2.xyw = tmpvar_1.xyw;
  tmpvar_3.xyw = tmpvar_5.xyw;
  tmpvar_2.z = (tmpvar_4.z + WaterFallSliceOffsset.y);
  tmpvar_3.z = (tmpvar_5.z + WaterFallSliceOffsset.y);
  highp vec2 tmpvar_7;
  tmpvar_7.x = tmpvar_2.x;
  tmpvar_7.y = WaterFallSliceOffsset.y;
  highp float tmpvar_8;
  highp vec2 _texCoord_9;
  highp vec2 tmpvar_10;
  tmpvar_10 = (1.0/(WorldXZOffsetSize.zw));
  _texCoord_9 = ((tmpvar_7 - WorldXZOffsetSize.xy) * tmpvar_10);
  _texCoord_9.x = mix (TextureDimensionsInv.x, TextureDimensionsInv.z, _texCoord_9.x);
  _texCoord_9.y = mix (TextureDimensionsInv.y, TextureDimensionsInv.w, _texCoord_9.y);
  lowp vec4 tmpvar_11;
  tmpvar_11 = texture2DLod (HeightMapTexture, _texCoord_9, 0.0);
  tmpvar_8 = tmpvar_11.x;
  tmpvar_2.y = clamp ((tmpvar_4.y * tmpvar_8), WaterFallSliceOffsset.z, WaterFallSliceOffsset.w);
  highp vec2 tmpvar_12;
  tmpvar_12.x = tmpvar_3.x;
  tmpvar_12.y = WaterFallSliceOffsset.y;
  highp float tmpvar_13;
  highp vec2 _texCoord_14;
  _texCoord_14 = ((tmpvar_12 - WorldXZOffsetSize.xy) * tmpvar_10);
  _texCoord_14.x = mix (TextureDimensionsInv.x, TextureDimensionsInv.z, _texCoord_14.x);
  _texCoord_14.y = mix (TextureDimensionsInv.y, TextureDimensionsInv.w, _texCoord_14.y);
  lowp vec4 tmpvar_15;
  tmpvar_15 = texture2DLod (HeightMapTexture, _texCoord_14, 0.0);
  tmpvar_13 = tmpvar_15.x;
  tmpvar_3.y = clamp ((tmpvar_5.y * tmpvar_13), WaterFallSliceOffsset.z, WaterFallSliceOffsset.w);
  highp vec4 tmpvar_16;
  highp vec2 tmpvar_17;
  highp vec4 outPosition_18;
  highp vec4 tmpvar_19;
  tmpvar_19.w = 1.0;
  tmpvar_19.xyz = tmpvar_2.xyz;
  highp vec4 tmpvar_20;
  tmpvar_20 = (tmpvar_19 * matWorldViewProj);
  outPosition_18 = (tmpvar_20 / abs(tmpvar_20.w));
  highp vec4 tmpvar_21;
  tmpvar_21.w = 1.0;
  tmpvar_21.xyz = tmpvar_3.xyz;
  highp vec4 tmpvar_22;
  tmpvar_22 = (tmpvar_21 * matWorldViewProj);
  highp vec2 tmpvar_23;
  tmpvar_23 = normalize(((tmpvar_22 / 
    abs(tmpvar_22.w)
  ).xy - outPosition_18.xy));
  outPosition_18.x = (outPosition_18.x - ((tmpvar_6.x * screenSize.z) * tmpvar_23.y));
  outPosition_18.y = (outPosition_18.y + ((tmpvar_6.x * screenSize.w) * tmpvar_23.x));
  tmpvar_16.xyw = outPosition_18.xyw;
  tmpvar_16.z = (outPosition_18.z - (outPosition_18.z * 1e-5));
  tmpvar_17.x = (0.5 * (1.0 - (
    (vTexCoord0.z - 2.0)
   / vTexCoord0.z)));
  tmpvar_17.y = tmpvar_6.y;
  gl_Position = tmpvar_16;
  xlv_COLOR0 = mix (vColor, vColor1, vTexCoord1.yyyy);
  xlv_TEXCOORD0 = tmpvar_17;
  xlv_TEXCOORD1 = (vec2(1.0, 1.0) - clamp (abs(
    ((1.0 - ((
      mix (tmpvar_2.y, WaterFallSliceOffsset.y, WaterFallTexCoordCalcParams.x)
     - WaterFallTexCoordCalcParams.y) / WaterFallTexCoordCalcParams.z)) - WaterFallTexCoordCalcParams.w)
  ), 0.0, 1.0));
}


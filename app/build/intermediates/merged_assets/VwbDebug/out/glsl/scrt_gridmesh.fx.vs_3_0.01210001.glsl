struct sGridParams {
  highp vec4 ContourScale;
  highp vec4 ContourOffset;
  highp vec4 ContourThickness;
  highp vec4 ContourColorX;
  highp vec4 ContourColorY;
  highp vec4 ContourColorZ;
  highp vec4 Params;
  highp vec4 SurfaceNormal;
  highp vec4 Lighting;
};
uniform highp mat4 matProj;
uniform highp mat4 matWorld;
uniform highp mat4 matWorldView;
uniform sampler2D HeightMapTexture;
uniform highp vec4 TextureDimensionsInv;
uniform highp vec4 MeshCellOfssetScalePosition;
uniform highp vec4 MeshCellOfssetScaleTexCoord;
uniform highp vec4 MeshCellOfssetScaleHeights;
uniform sGridParams GridParams;
attribute highp vec4 vPosition;
attribute highp vec4 vTexCoord0;
varying highp vec4 xlv_TEXCOORD0;
varying highp vec4 xlv_TEXCOORD1;
varying highp vec3 xlv_TEXCOORD2;
varying highp vec4 xlv_TEXCOORD4;
varying highp vec4 xlv_TEXCOORD5;
varying highp float xlv_TEXCOORD6;
varying highp vec4 xlv_TEXCOORD7;
void main ()
{
  highp vec4 tmpvar_1;
  highp vec4 tmpvar_2;
  tmpvar_1.w = vPosition.w;
  tmpvar_2.zw = vTexCoord0.zw;
  highp vec4 tmpvar_3;
  highp vec4 tmpvar_4;
  highp vec4 tmpvar_5;
  highp vec4 tmpvar_6;
  tmpvar_1.xz = ((vPosition.xz * MeshCellOfssetScalePosition.xy) + MeshCellOfssetScalePosition.zw);
  highp vec2 _texCoord_7;
  _texCoord_7 = ((vTexCoord0.xy * MeshCellOfssetScaleTexCoord.xy) + MeshCellOfssetScaleTexCoord.zw);
  _texCoord_7 = mix (TextureDimensionsInv.xy, TextureDimensionsInv.zw, _texCoord_7);
  lowp vec4 tmpvar_8;
  tmpvar_8 = texture2DLod (HeightMapTexture, _texCoord_7, 0.0);
  tmpvar_1.y = (tmpvar_8.x * vPosition.w);
  highp vec3 sampledNormal_9;
  highp vec3 tmpvar_10;
  tmpvar_10.z = 0.0;
  tmpvar_10.xy = TextureDimensionsInv.xy;
  highp float tmpvar_11;
  highp vec2 _texCoord_12;
  _texCoord_12 = (vTexCoord0.xy - tmpvar_10.xz);
  _texCoord_12 = ((_texCoord_12 * MeshCellOfssetScaleTexCoord.xy) + MeshCellOfssetScaleTexCoord.zw);
  lowp vec4 tmpvar_13;
  tmpvar_13 = texture2DLod (HeightMapTexture, _texCoord_12, 0.0);
  tmpvar_11 = tmpvar_13.x;
  highp float tmpvar_14;
  highp vec2 _texCoord_15;
  _texCoord_15 = (vTexCoord0.xy + tmpvar_10.xz);
  _texCoord_15 = ((_texCoord_15 * MeshCellOfssetScaleTexCoord.xy) + MeshCellOfssetScaleTexCoord.zw);
  lowp vec4 tmpvar_16;
  tmpvar_16 = texture2DLod (HeightMapTexture, _texCoord_15, 0.0);
  tmpvar_14 = tmpvar_16.x;
  highp float tmpvar_17;
  highp vec2 _texCoord_18;
  _texCoord_18 = (vTexCoord0.xy - tmpvar_10.zy);
  _texCoord_18 = ((_texCoord_18 * MeshCellOfssetScaleTexCoord.xy) + MeshCellOfssetScaleTexCoord.zw);
  lowp vec4 tmpvar_19;
  tmpvar_19 = texture2DLod (HeightMapTexture, _texCoord_18, 0.0);
  tmpvar_17 = tmpvar_19.x;
  highp float tmpvar_20;
  highp vec2 _texCoord_21;
  _texCoord_21 = (vTexCoord0.xy + tmpvar_10.zy);
  _texCoord_21 = ((_texCoord_21 * MeshCellOfssetScaleTexCoord.xy) + MeshCellOfssetScaleTexCoord.zw);
  lowp vec4 tmpvar_22;
  tmpvar_22 = texture2DLod (HeightMapTexture, _texCoord_21, 0.0);
  tmpvar_20 = tmpvar_22.x;
  sampledNormal_9.x = (((tmpvar_11 * MeshCellOfssetScaleHeights.x) + MeshCellOfssetScaleHeights.y) - ((tmpvar_14 * MeshCellOfssetScaleHeights.x) + MeshCellOfssetScaleHeights.y));
  sampledNormal_9.y = 2.0;
  sampledNormal_9.z = (((tmpvar_17 * MeshCellOfssetScaleHeights.x) + MeshCellOfssetScaleHeights.y) - ((tmpvar_20 * MeshCellOfssetScaleHeights.x) + MeshCellOfssetScaleHeights.y));
  highp vec3 tmpvar_23;
  tmpvar_23 = normalize(sampledNormal_9);
  sampledNormal_9 = tmpvar_23;
  highp mat3 tmpvar_24;
  tmpvar_24[0] = matWorld[0].xyz;
  tmpvar_24[1] = matWorld[1].xyz;
  tmpvar_24[2] = matWorld[2].xyz;
  highp vec2 _texCoord_25;
  _texCoord_25 = ((vTexCoord0.zw * MeshCellOfssetScaleTexCoord.xy) + MeshCellOfssetScaleTexCoord.zw);
  _texCoord_25 = mix (TextureDimensionsInv.xy, TextureDimensionsInv.zw, _texCoord_25);
  lowp vec4 tmpvar_26;
  tmpvar_26 = texture2DLod (HeightMapTexture, _texCoord_25, 0.0);
  highp float tmpvar_27;
  tmpvar_27 = GridParams.Params.w;
  highp float tmpvar_28;
  tmpvar_28 = mix (tmpvar_1.y, (tmpvar_26.x * vPosition.w), tmpvar_27);
  tmpvar_2.xy = mix (vTexCoord0.xy, vTexCoord0.zw, vec2(tmpvar_27));
  tmpvar_6 = tmpvar_2;
  tmpvar_2.xy = clamp (tmpvar_2.xy, 0.0, 1.0);
  tmpvar_2.xy = mix (TextureDimensionsInv.xy, TextureDimensionsInv.zw, tmpvar_2.xy);
  tmpvar_2.xy = ((tmpvar_2.xy * MeshCellOfssetScaleTexCoord.xy) + MeshCellOfssetScaleTexCoord.zw);
  tmpvar_1.y = ((tmpvar_1.y * MeshCellOfssetScaleHeights.x) + MeshCellOfssetScaleHeights.y);
  tmpvar_5 = tmpvar_1;
  tmpvar_1.xz = tmpvar_1.xz;
  tmpvar_1.w = 1.0;
  tmpvar_3 = (tmpvar_1 * (matWorldView * matProj));
  tmpvar_4.xy = tmpvar_2.xy;
  tmpvar_4.zw = tmpvar_2.zw;
  gl_Position = tmpvar_3;
  xlv_TEXCOORD0 = tmpvar_4;
  xlv_TEXCOORD1 = tmpvar_5;
  xlv_TEXCOORD2 = (tmpvar_23 * tmpvar_24);
  xlv_TEXCOORD4 = (tmpvar_1 * matWorld);
  xlv_TEXCOORD5 = tmpvar_3;
  xlv_TEXCOORD6 = tmpvar_28;
  xlv_TEXCOORD7 = tmpvar_6;
}
